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
        const { items, storeName } = req.body; // Assuming items and storeName are in the request body
        if (!items || !storeName) {
            return res.status(400).json({ error: "items or storeName not provided" });
        }
        const totalPrice = await priceService.getTotalPriceByStore(items, storeName);
        res.status(200).json({ totalPrice });
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: error.message });
    }
};



module.exports = { getItemPriceByStore, getTotalPriceByStore };