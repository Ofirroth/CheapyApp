const mongoose = require('mongoose');
const Item = require('./item');
const Schema = mongoose.Schema;

const cartItemSchema = new Schema({
  name: { type: String, required: true },
  categoryId: { type: Number, required: true },
  itemPic: { type: String, required: false },
  category: { type: String, required: true },
  _id: { type: String, required: false },
  quantity: { type: Number, required: true },
  price: { type: Number, required: true },
});



// Define the Cart schema
const cart = new Schema({
  storeId: { type: String, required: true },
  totalPrice: { type: Number, required: true },
  dateCreated: { type: Date, required: true},
  items: { type: [cartItemSchema], required: true},
  userId: { type: String, required: true },
});

const Cart = mongoose.model('Cart', cart);

module.exports = Cart;
