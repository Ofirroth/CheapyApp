const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const subCategory = new Schema({
  name: { type: String, required: true },
  id: { type: Number, required: true, unique: true },
  parent: { type: Number, required: true },
  image: { type: String, required: true }
});

const SubCategory = mongoose.model('SubCategory', subCategory);

module.exports = SubCategory;