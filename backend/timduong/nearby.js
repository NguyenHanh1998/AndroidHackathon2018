// const mlways = require ('../models/mlways');
var Waypoints = require ('../models/waypoints');

async function nearestPoint(Lat, Lng) {
    near = {}
    points = await Waypoints.find({});

    var map = []

    points.forEach((point) => {
        var newpoint = {}
        newpoint.key = point
        let lat = Number(point.lat)
        let lng = Number(point.lng)
        newpoint.value = (Lat-lat)*(Lat-lat) + (Lng-lng)*(Lng-lng)
        map.push(newpoint)
    })

    var min = map[0]

    map.forEach((object) => {
        min = (min.value > object.value) ? object : min
    })

    near = min

    return near.key._id;
}
 
module.exports = nearestPoint;