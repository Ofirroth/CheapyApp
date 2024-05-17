const messageService = require('../services/message');
const userService = require('../services/user');
const Message = require('../models/message');
const Chat = require('../models/chat');
const User = require('../models/user');
const tokenMap = require('../tokenMap');
const socketMap = require('../socketMap');
const admin = require('firebase-admin');
const serviceAccount = require('../cheapy-firebase.json');

const createMessage = async (req, res) => {
    try {
        if (req.headers.authorization) {
            // Extract the username from that header
            const token = req.headers.authorization.split(" ")[1];
            const result = await userService.getUserByToken(token);
            if (!result) {
                return res.status(404).json("user not found");
            } else {
                const { id } = req.params;
                const msg = req.body.msg;
                const message = await messageService.createMessage(id, result.username, msg);
                if(message){
                    const chat = await Chat.findOne({id:req.params.id});
                    if(chat.users[0].username === result.username){
                        var talkingTo = chat.users[1];
                    }
                    else{
                        var talkingTo = chat.users[0];
                    }
                
                    if(tokenMap.get(talkingTo.username)){
                        delete socketMap.get(talkingTo.username);
                        const registrationToken = tokenMap.get(talkingTo.username);
                        const name = await User.findOne({username: message.sender.username});
                        const msgNotific = {
                                notification: {
                                        title: name.displayName,
                                        body: message.content,
                                    },
                                data: {
                                        action: 'send_message',
                                        senderUserName: message.sender.username.toString(),
                                        senderDisplayName: name.displayName.toString(),
                                        reciver: talkingTo.toString(),
                                        data_date: message.created.toISOString(),
                                        msgId: message.id.toString(),
                                        chatId: id.toString()
                                    },
                                token: registrationToken,
                            };
                            admin.messaging().send(msgNotific)
                                .then((response) => {
                            console.log('Successfully sent message:', response);
                    })
                    .catch((error) => {
                        console.log('Error sending message:', error);
                    });
                    }
                    else if (socketMap.get(talkingTo.username)) {
                        var x = req.params.id;
                        const newMsg =
                            {  
                                chatId: x,
                                receiverUser: talkingTo.username,
                                message: {
                                    created: message.created,
                                    sender: message.sender,
                                    content: message.content,
                                    _id: message.id.toString(),
                                    __v: 0
                                }
                            }
                        await socketMap.get(talkingTo.username).emit("receive_message",newMsg);
                    }
                    return res.status(200).json(message);
                }
            }
        }
    } catch (error) {
        if (error.message === "user not  found") {
            return res.status(404).json("user not  found");
        }
        else if (error.message === "invalid token"){
            return res.status(404).json("invalid token");
        }
        else {
            console.log(error);
            throw new Error('Internal Server Error');

        }
    }
};

const getMessages = async (req, res) => {
    try {
        if (req.headers.authorization) {
            // Extract the username from that header
            const token = req.headers.authorization.split(" ")[1];
            const result = await userService.getUserByToken(token);
            if (!result) {
                return res.status(404).json("no user Found");
            }
            else {
                const { id } = req.params;
                const x = await messageService.getMessages(id);
                return res.json(await messageService.getMessages(id));
            }
        }
    } catch (error) {
        if (error.message === "user not  found") {
            return res.status(404).json("user not  found");
        }
        else if (error.message === "invalid token"){
            return res.status(404).json("invalid token");
        }
        else
            throw new Error('Internal Server Error');
    }
};

module.exports = { createMessage, getMessages };