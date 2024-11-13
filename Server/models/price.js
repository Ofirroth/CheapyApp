const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const Price = new Schema({
    itemId: {
        type: String,
        required: true
    },
    price: {
         type: Number,
         required: true
        },
    storeId: {
         type: String,
         required: true
    },
});

module.exports = mongoose.model('Price', Price);


