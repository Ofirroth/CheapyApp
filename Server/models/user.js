const mongoose =require ('mongoose');
const Schema = mongoose.Schema;

const User = new Schema({
    username:{
        type:  String,
        nullable: true
    },
    displayName: {
        type: String,
        nullable: true
    },
    profilePic:{
        type: String,
        nullable: true
    }
});
module.exports = mongoose.model('User', User);