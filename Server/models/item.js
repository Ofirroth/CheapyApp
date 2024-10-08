const mongoose =require ('mongoose');
const Store = require('./store');
const Schema = mongoose.Schema;

const Item = new Schema({
    name:{
           type:  String,
           nullable: false
    },
    itemPic:{
           type: String,
           nullable: true
    },
    price: {
        type: Number,
        nullable: false
    },
    store: {
        type: Store
        nullable: true
    }
});
module.exports = mongoose.model('Item', Item);