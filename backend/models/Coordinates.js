const mongoose = require('mongoose');

const Coordinates1 = new mongoose.Schema({
    start_lat : String,
    start_lng : String,
    end_lat : String,
    end_lng : String,
},{ timestamps: true});

module.exports = mongoose.model('Coordinates',Coordinates1);