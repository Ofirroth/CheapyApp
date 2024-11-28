const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const User = new Schema({
    username: {
        type: String,
        required: true
    },
    displayName: {
        type: String,
        required: false
    },
    profilePic: {
        type: String,
        required: false
    },
    homeAddress: {
        type: String,
        required: false
    },
    workAddress: {
            type: String,
            required: false
        },
        mail: {
                type: String,
                required: false
            },
            phone: {
                    type: String,
                    required: false
                },
});

module.exports = mongoose.model('User', User);
