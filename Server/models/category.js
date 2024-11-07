const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const categorySchema = new Schema({
  name: { type: String, required: true },
  customId: { type: Number, required: true, unique: true },
  sub: { type: [String], default: [] },
  image: { type: String, required: true }
});

const Category = mongoose.model('Category', categorySchema);

module.exports = Category;