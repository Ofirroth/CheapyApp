const Item = require('../models/item');

const getItemsByCategory = async (category) => {
    try {
        const items = await Item.find({ category }).populate('store');  // Populates store data
        return items;
    } catch (error) {
        throw new Error('Error fetching items');
    }
};

const getAllItems = async () => {
    try {
        const items = await Item.find()
        return items;
    } catch (error) {
        throw new Error('Error fetching items');
    }
};

module.exports = { getItemsByCategory, getAllItems };
