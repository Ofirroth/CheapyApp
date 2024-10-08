const mongoose =require ('mongoose');
const Order = require('./order');
const Group = require('./group');
const Schema = mongoose.Schema;

const User = new Schema({
    username:{
        type:  String,
        nullable: true
    },
    displayName: {
        type: String,
        nullable: true
    },
    profilePic:{
        type: String,
        nullable: true
    },
    city:{
            type: String,
            nullable: true
    },
    orderHistory: {
        type: [Order.schema],
        nullable: true
    },
    order: {
        type: Order,
        nullable: true
    },
    group: {
        type: Group,
        nullable: true
    }

});
module.exports = mongoose.model('User', User);