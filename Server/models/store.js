const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Store = new Schema({
    name: {
        type: String,
        required: true
    },
    city: {
        type: String,
        required: true
    },
    image: {
        type: String,
        required: true
    },
    url: {
        type: String,
        required: true
    },
    searchButtonSelector: {
        type: Schema.Types.Mixed,
        required: true,
    },
    searchInputSelector: {
        type: String,
        required: true,
    },
    priceSelector: {
        type: String,
        required: true,
    },
    productStripSelector: {
        type: String,
        required: true,
    },
    closeButtonSelector: {
            type: String,
            required: true,
    },
});

module.exports = mongoose.model('Store', Store);