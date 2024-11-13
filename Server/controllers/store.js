const storeService = require('../services/store');

const getStores = async (req, res) => {
    try {
        const stores = await storeService.getAllStores();
        res.status(200).json(stores);
    } catch (error) {
        console.error(error);
        res.status(500).json('Internal Server Error');
    }
};

const getTotalPriceByStore = async (req, res) => {
    try {
        const { storeName, items } = req.body;
        if (!storeName) {
            console.error("Store name is missing");
            return res.status(400).json({ error: "Store name is required" });
        }
        const totalPrice = await storeService.getTotalPriceByStoreName(storeName, items);
        res.status(200).json(totalPrice);
    } catch (error) {
        console.error("Error calculating total price for store", error);
        res.status(500).json({ error: "Internal Server Error" });
    }
};

module.exports = { getStores, getTotalPriceByStore };