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

async function getStoreByCity(req, res) {
    const { city } = req.params;
    try {
        const stores = await storeService.getStoresByCity(city);
        if (stores.length === 0) {
            return res.status(404).json({ message: 'No stores found in the specified city' });
        }
        res.status(200).json(stores);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}


module.exports = { getStores, getTotalPriceByStore, getStoreByCity };