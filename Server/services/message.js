
const Message = require('../models/message');
const chat = require('../models/chat')
const userService = require('../services/user');
const chatCounterSchema = require('../models/chatCounterSchema');

const createMessage = async (id, userName, content) => {
    try {
        const chats = await chat.findOne({ id });
        // find the id
        if (chats) {
            const time = new Date(); // format the time as a string
            const sender1 = await userService.getUserByName(userName);
            if (!sender1) {
                throw new Error("UserName not exist");
            }
            const message = new Message({ created: time, sender:{username:sender1.username}, content: content });
            const savedMessage = await Message.create(message);
            chats.messages.push(savedMessage);
            await chats.save();
            return await savedMessage.save();
        }
        else {
             throw new Error("chat not found"); 
            }
    } catch (error) {
        if (error.message === "UserName not exist" || error.message === "chat not found")
            throw error;
        else
            throw new Error('Internal Server Error');
    }
}

const getMessages = async (id) => {
    try {
        const Chats = await chat.findOne({ id });
        if (Chats) {
            return Chats.messages;
        }
        else {
            throw new Error("chat not found"); 
        }
    } catch (error) {
        if (error.message === "chat not found")
            throw error;
        else
            throw new Error('Internal Server Error');
    }
};

module.exports = { createMessage, getMessages };