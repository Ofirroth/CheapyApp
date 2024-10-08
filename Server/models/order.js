const mongoose =require ('mongoose');
const User = require('./user');
const Item = require('./item');
const Group = require('./group');
const Store = require('./store');
const Schema = mongoose.Schema;

const Order = new Schema({
    cart: {
        type: [Item.schema],
    }
    confirmDate: {
        type: Date,
        nullable: true
    }
    store: {
        type: Store,
        nullable: true
    }
    user: {
        type: User,
        nullable: true
    },
    group: {
        type: Group,
        nullable: true
    }

});
module.exports = mongoose.model('Order', Order);