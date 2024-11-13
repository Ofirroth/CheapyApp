const priceService = require('../services/price');

const getItemPriceByStore = async (req, res) => {
    try {
        const { itemId, storeName } = req.params;
        const price = await priceService.getItemPriceByStore(itemId, storeName);
        console.log(price);
        res.status(200).json(price);
    } catch (error) {
        console.error(error);
        res.status(500).json('Internal Server Error');
    }
};

const getTotalPriceByStore = async (req, res) => {
    try {
        const { storeName, items } = req.params;
        const totalPrice = await priceService.getTotalPriceByStore(items, storeName);
        console.log(totalPrice);
        res.status(200).json({ totalPrice });
    } catch (error) {
        console.error(error);
        res.status(500).json('Internal Server Error');
    }
};


module.exports = { getItemPriceByStore, getTotalPriceByStore };