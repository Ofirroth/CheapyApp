const mongoose = require('mongoose');
const Store = require('./store');
const Category = require('./category');

const Schema = mongoose.Schema;

const Item = new Schema({
    name: {
        type: String,
        required: true
    },
    itemPic: {
        type: String,
        required: false
    },
    price: {
        type: Number,
        required: true
    },
    store: {
        type: Schema.Types.ObjectId,
        ref: 'Store',
        required: false
    },
    category: {
        type: String,
        required: true
    },
    subCategory: {
            type: String,
            required: false
      }
});

module.exports = mongoose.model('Item', Item);
