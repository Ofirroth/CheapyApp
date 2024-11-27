const express = require('express');
const router = express.Router();
const recoController = require('../controllers/reco');

// Route to handle GET requests for recommendations by username
router.get('/:username', recoController.getRecommended);

// Export the router to be used in other parts of the application
module.exports = router;