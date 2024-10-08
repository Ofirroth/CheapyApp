const mongoose =require ('mongoose');
const User = require('./user');
const Schema = mongoose.Schema;

const Group = new Schema({
    name:{
            type: String,
            nullable: true
    },
    users: {
        type: [User.schema],
    }

});
module.exports = mongoose.model('Group', Group);
