const Item = require('../models/item');

const getItemsByCategory = async (categoryId) => {
    try {
        const items = await Item.find({ categoryId: categoryId });
        return items;
    } catch (error) {
        throw new Error('Error fetching items by category');
    }
};

const getAllItems = async () => {
    try {
        const items = await Item.find();
        return items;
    } catch (error) {
        throw new Error('Error fetching items');
    }
};

const getAllItemsNames = async () => {
    try {
        const items = await Item.find().select('name');  // Select only the 'name' field
        return items.map(item => item.name);
    } catch (error) {
        throw new Error('Error fetching items');
    }
};

module.exports = { getItemsByCategory, getAllItems, getAllItemsNames };
