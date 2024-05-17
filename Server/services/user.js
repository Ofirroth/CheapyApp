const User = require('../models/user')
const UserPassName = require('../models/UserPassName')
const jwt = require('jsonwebtoken');

const createUser = async (username, password, displayName, profilePic) => {
    try {
        const users = await getAllUsers();
        const user = users.find((user) => user.username === username);
        if (!user) {
            const newUserPass = new UserPassName({ username: username, password: password, displayName: displayName, profilePic: profilePic });
            UserPassName.create(newUserPass);
            const newuser = new User({ username: username, displayName: displayName, profilePic: profilePic });
            const savedUser = await User.create(newuser);
            return savedUser;
        }
        //in case there is already user in this name
        else {
            throw new Error("UserName already exist");
        }
    }
    catch (error) {
        if (error.message === "UserName already exist") 
            throw error;
        else 
            throw new Error('Internal Server Error');
    }
}

const getAllUsers = async () => {
    try {
        return await User.find({});
    } catch (error) {
        throw new Error('Internal Server Error');
    }
}

const getAllUsersPassName = async () => {
    try {
        return await UserPassName.find({});
    }
    catch (error) {
        throw new Error('Internal Server Error');
    }
}


const getUserByToken = async (token) => {
    try {
        // Verify the token is va
        const data = jwt.verify(token, "Some super secret key shhhhhhhhhhhhhhhhh!!!!!");
        return await User.findOne({ username: data.username });
    }
    catch (error) {
        throw new Error('Internal Server Error');
    }
}

const getUserByName = async (userName) => {
    try {
        const users = await getAllUsers();
        const user = users.find((user) => user.username === userName);
        if (!user) {
            throw new Error('UserName not found');
        }
        else {
            return user;
        }
    }
    catch (error) {
        if (error.message == "UserName not found") 
            throw error;
        else 
            throw new Error('Internal Server Error');
    }
}


module.exports = { createUser, getAllUsers, getUserByToken, getAllUsersPassName, getUserByName };

