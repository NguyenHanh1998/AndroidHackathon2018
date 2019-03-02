const mongoose = require('mongoose');


const Waypoints = new mongoose.Schema({
    _id: Number,
    place: String,
    lat: Number,
    lng: Number
},{ timestamps: true});

module.exports = mongoose.model('Waypoints',Waypoints);