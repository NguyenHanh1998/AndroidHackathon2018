const Graph = require('node-dijkstra');
const mlways = require ('../models/mlways');

async function createMap(idstart,idend) {
    var map = {};
    var points= await mlways.find({}) 

    points.forEach((point) => {
        let node = {}
        node[point.endWaypointID] = point.time
        if (map[point.startWaypointID] != undefined) {
            let hih = Object.assign({}, map[point.startWaypointID], node)
            map[point.startWaypointID] = hih
        } else {
            map[point.startWaypointID] = node
        }
        
    })
    
    points.forEach((point) => {
        let node = {}
        node[point.startWaypointID] = point.time
        if (map[point.endWaypointID] != undefined) {
            let hih = Object.assign({}, map[point.endWaypointID], node)
            map[point.endWaypointID] = hih
        } else {
            map[point.endWaypointID] = node
        }
    })

    //console.log(map)
    var graph = new Graph(map)
    var kq= graph.path(String(idstart), String(idend), { cost: true })
    return kq;
}

module.exports = createMap;
