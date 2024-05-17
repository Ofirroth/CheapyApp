const mongoose = require('mongoose');
const User = require('./user');
const Message = require('./message');

const Schema = mongoose.Schema;

const Chat = new Schema({
  id: {
    type: Number,
  },
  users: {
    type: [User.schema],
  },
  messages: {
    type: [Message.schema],
  }
});

module.exports = mongoose.model('Chat', Chat);
