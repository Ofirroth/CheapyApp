const mongoose =require ('mongoose');
const Schema = mongoose.Schema;

const Store = new Schema({
    name:{
            type: String,
            nullable: true
        },
    city:{
                type: String,
                nullable: true
        }

});
module.exports = mongoose.model('Store', Store);