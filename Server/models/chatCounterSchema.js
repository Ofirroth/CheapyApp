const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const chatCounterSchema = new Schema({
    count: {
      type: Number,
      default: 0
    }
  });
  
  module.exports = mongoose.model('ChatCounter', chatCounterSchema);