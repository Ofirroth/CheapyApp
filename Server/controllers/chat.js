const chatService = require('../services/chat');
const userService = require('../services/user');
const tokenService = require('../services/token');
const User = require('../models/user');
const Chat = require('../models/chat');
const tokenMap = require('../tokenMap');
const socketMap = require('../socketMap');
const admin = require('firebase-admin');
const serviceAccount = require('../cheapy-firebase.json');

const createChat = async (req, res) => {
    try {
        if (req.headers.authorization) {
            // Extract the username from that header
            const token = req.headers.authorization.split(" ")[1];
            const result = await userService.getUserByToken(token);
            if (!result) {
                throw new Error("invalid token");
            } else {
                if(result.username === req.body.username){
                    throw new Error("Not allow to add yourself as contact");
                }
                const newChat = await chatService.createChat(result.username, req.body.username);
                if (newChat) {
                    const chat = await Chat.findOne({
                        $and: [
                          { 'users.username': result.username },
                          { 'users.username': req.body.username }
                        ]
                      });
                    if(chat.users[0].username === result.username){
                        var talkingTo = chat.users[1];
                        var talkingfrom = chat.users[0];
                    }
                    else{
                        var talkingTo = chat.users[0];
                        var talkingfrom = chat.users[1];

                    }
                    if(tokenMap.get(talkingTo.username)){
                        //delete socketMap.get(talkingTo.username);
                        const registrationToken = tokenMap.get(talkingTo.username);
                        const msgNotific = {
                                notification: {
                                        title: 'new chat',
                                        body: talkingTo.username,
                                    },
                                data: {
                                        action: 'add_contact',
                                        UserName: talkingfrom.username.toString(),
                                        DisplayName: talkingfrom.displayName.toString(),
                                        chatID:chat.id.toString()
                                    },
                                token: registrationToken,
                            };
                            admin.messaging().send(msgNotific)
                                .then((response) => {
                            console.log('Successfully sent message:', response);
                    }).catch((error) => {
                        console.log('Error sending message:', error);
                    });
                }
                else if (socketMap.get(talkingTo.username)) {
                    await socketMap.get(talkingTo.username).emit("add-contact");
                }
                return res.status(200).json(newChat);
        }
    }
    }
 } catch (error) {   
        if (error.message === "Invalid token") {
            res.status(401).json("Invalid token");
        }
        else if (error.message === "UserName not exist") {
            return res.status(404).json("UserName not exist");
        }
        else if(error.message === "Not allow to add yourself as contact"){
            return res.status(404).json("Not allow to add yourself as contact");
        }
        else if (error.message === "This contact already exist") {
            return res.status(404).json("This contact already exist") ;
        }
        else {
            return res.status(500).json('Internal Server Error');
        }
    }
};



const getChats = async (req, res) => {
    try {
        if (req.headers.authorization) {
            // Extract the token from that header
            const token = req.headers.authorization.split(" ")[1];
            res.json(await chatService.getChats(token));
        }
    }
    catch (error) {
        if (error.message === "UserName not exist")
            return res.status(404).json("UserName not exist");
        else
            return res.status(500).json('Internal Server Error');
    }
};

const getChat = async (req, res) => {
    try {
        const chat = await chatService.getChatById(req.params.id);
        if (!chat) {
            return res.status(404).json('Chat not found');
        }
        res.json(chat);
    } catch (error) {
        res.status(500).send('Internal Server Error');
    }
};

const deleteChat = async (req, res) => {
    try {
        const chat = await chatService.getChatById(req.params.id);
        if (!chat) {
            return res.status(404).json('Chat not found');
        }
        else {
            return res.status(200).json("Chat removed");
        }
    }
    catch (error) {
        res.status(500).send('Internal Server Error');
    }
};

module.exports = { createChat, getChats, getChat, deleteChat };