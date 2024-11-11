const itemService = require('../services/item');

const getItems = async (req, res) => {
    try {
        const items = await itemService.getAllItems();
        console.log(items);
        res.status(200).json(items);
    } catch (error) {
        console.error(error);
        res.status(500).json('Internal Server Error');
    }
};

module.exports = { getItems };