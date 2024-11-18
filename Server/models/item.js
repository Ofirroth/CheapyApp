const mongoose = require('mongoose');
const Store = require('./store');
const Category = require('./category');

const Schema = mongoose.Schema;

const Item = new Schema({
    name: {
        type: String,
        required: true
    },
    categoryId: {
        type: Number,
        required: true
    },
    itemPic: {
        type: String,
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
