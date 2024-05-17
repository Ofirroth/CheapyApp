const userService = require('../services/user');

const createUser = async (req, res) => {
    try {
        const user = await userService.createUser(req.body.username, req.body.password, req.body.displayName, req.body.profilePic);
        res.status(200).json(user);
    }
    catch (error) {
        if (error.message == "UserName already exist") 
            res.status(409).json("UserName already exist");
        else {
            res.status(500).send('Internal Server Error');
        }
    }
};

const getAllUsers = async (req, res) => {
    try {
        res.json(await userService.getAllUsers());
    }
    catch (error) {
        res.status(500).send('Internal Server Error');
    }
};

const getUserByToken = async (req, res) => {
    try {
        if (req.headers.authorization) {
            // Extract the token from that header
            const token = req.headers.authorization.split(" ")[1];
            const result = await userService.getUserByToken(token);
            if (!result)
                return res.status(404).json("User not found");
            else
                return res.json(result);
        } else {
            res.status(500).send('Internal Server Error');
        }
    }
    catch (error) {
        res.status(500).send('Internal Server Error');
    }
}

module.exports = { createUser, getAllUsers, getUserByToken }; 
