const mongoose = require('mongoose');
const item = require('./item');
const Schema = mongoose.Schema;

const cartItemSchema = new Schema({
  item: { type: Schema.Types.ObjectId, ref: 'Item', required: true },
  quantity: { type: Number, required: true },
  price: { type: Number, required: true },
});

// Define the Cart schema
const cart = new Schema({
  storeId: { type: String, required: true },
  totalPrice: { type: Number, required: true },
  dateCreated: { type: Date, required: true},
  items: [cartItemSchema],
  userId: { type: String, required: true },
});

const Cart = mongoose.model('Cart', cart);

module.exports = Cart;
