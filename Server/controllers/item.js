const itemService = require('../services/item');

const getItems = async (req, res) => {
    try {
        const items = await itemService.getAllItems();
        res.status(200).json(items);
    } catch (error) {
        console.error(error);
        res.status(500).json('Internal Server Error');
    }
};

const getItemsByCategory = async (req, res) => {
    try {
        const { category } = req.params;
        const items = await itemService.getItemsByCategory(category);
        res.status(200).json(items);
    } catch (error) {
        res.status(500).json('Internal Server Error');
    }
};


module.exports = { getItems, getItemsByCategory };