const mongoose = require('mongoose');
const Order = require('./order');  // Import the Order model
const Group = require('./group');  // Import the Group model
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
    city: {
        type: String,
        required: false
    },
    orderHistory: {
        type: [Schema.Types.ObjectId],  // Use ObjectId to reference Order
        ref: 'Order',                    // Reference the Order model
        required: false
    },
    order: {
        type: Schema.Types.ObjectId,     // Use ObjectId to reference Order
        ref: 'Order',                    // Reference the Order model
        required: false
    },
    group: {
        type: Schema.Types.ObjectId,     // Use ObjectId to reference Group
        ref: 'Group',                    // Reference the Group model
        required: false
    }
});

module.exports = mongoose.model('User', User);
