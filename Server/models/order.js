const mongoose = require('mongoose');
const User = require('./user');
const Item = require('./item');
const Group = require('./group');
const Store = require('./store');
const Schema = mongoose.Schema;

const Order = new Schema({
    cart: {
        type: [
            {
                item: {
                    type: Schema.Types.ObjectId,
                    ref: 'Item',
                    required: true
                },
                quantity: {
                    type: Number,
                    required: true
                }
            }
        ]
    },
    confirmDate: {
        type: Date,
        required: false
    },
    store: {
        type: Schema.Types.ObjectId,
        ref: 'Store',
        required: false
    },
    user: {
        type: Schema.Types.ObjectId,
        ref: 'User',
        required: false
    },
    group: {
        type: Schema.Types.ObjectId,
        ref: 'Group',
        required: false
    }
});

module.exports = mongoose.model('Order', Order);
