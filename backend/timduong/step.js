var Waypoints = require ('../models/waypoints');

async function routeJson(source) {

    //console.log(source);
    let allPoint = {}
    let path = source.path
    
    await Promise.all(path.map(async (id) => {
        var res = await Waypoints.findById(Number(id));
        allPoint[id] = {
            lat : res.lat ,
            lng : res.lng
        }
    }));
    console.log(allPoint);
    
//     allPoint= { '1': { lat: 21.027233, lng: 105.787644 },
//   '2': { lat: 21.026233, lng: 105.788228 },
//   '3': { lat: 21.02464, lng: 105.789153 },
//   '4': { lat: 21.023266, lng: 105.79001 } }


    return allPoint;
   //console.log(allPoint2);
}


module.exports = routeJson;