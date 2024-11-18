const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Barcode = new Schema(
  {
    barcode: {
      type: String,
      required: true,
      unique: true,
    },
    productName: {
      type: String,
      required: true,
    },
  },
  { timestamps: true }
);

module.exports = mongoose.model('Barcode', Barcode);
