const recoService = require('../services/reco');

// Controller function to handle recommendation requests
const getRecommended = async (req, res) => {
    try {
        const { username } = req.params; // Extract username from request parameters
        const recoItems = await recoService.getRecommended(username); // Fetch recommended items
        res.status(200).json(recoItems); // Respond with recommended items as JSON
    } catch (error) {
        console.error(error);
        res.status(500).json('Internal Server Error');
    }
};

// export the function
module.exports = {getRecommended};