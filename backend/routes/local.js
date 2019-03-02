var express = require('express');
var mlways = require ('../models/mlways');
var Waypoints = require ('../models/waypoints');
var createMap = require ('../timduong/timduong');
var nearestPoint = require ('../timduong/nearby');
var routeJson = require ('../timduong/step'); 
var stepJson = require ('../timduong/aStep');

var router = express.Router();

router.get('/local',(req,res)=>{
    //console.log(req)
    let start_lat = Number(req.query.start_lat)
    let start_lng = Number(req.query.start_lng)
    let end_lat = Number(req.query.end_lat)
    let end_lng = Number(req.query.end_lng)

    nearestPoint(start_lat,start_lng).then((idstart)=>{
        nearestPoint(end_lat,end_lng).then((idend)=>{
            createMap(Number(idstart),Number(idend)).then((route)=>{ 
                console.log(route);
                routeJson(route).then((step)=>{
                    stepJson(step,route).then((astep)=>{
                        //console.log(astep);
                        res.json(astep);
                    })                    
                }) 
            })
        })
    });    

    
});

router.post('/update', async (req, res) => {
    console.log(req.body.val_left)

    await mlways.find({_id: Number(req.body._id)}, (err, all) => {
        //console.log(all)
        all.map( async (point) => {
            //console.log(point.distant)
            let time =  Number(point.distant)/Number(req.body.val_left)
            await mlways.findByIdAndUpdate(point._id, {
                time: Number(time)
            }, null)
        })
        
    })

    res.json(req.body);
})

router.post('/resetwaypoint', async (req, res) => {
    await mlways.find({}, (err, all) => {
        //console.log(all)
        all.map( async (point) => {
            //console.log(point.distant)
            let time = Number(point.distant)
            await mlways.findByIdAndUpdate(point._id, {
                time: Number(time)
            }, null)
        })
        
    })
    res.json({iSad: 'DONE'})
})

router.get('/maytram',(req,res)=>{
    res.json({title : 'tim cach ket noi nhe'});
})

router.post('/maytram', async (req,res)=>{
    console.log(req.body);
    /// TODO

    let all = String(req.body.all)
    let line = all.split('\n')

    for (var i = 0; i < line.length; i++) {
        let each = line[i].split('\t')
        let id = each[0]
        let startWaypointID = each[1]
        let endWaypointID = each[2]
        let distant = each[3]
        let time = each[4]

        console.log(time)
        

        var mlway = new mlways({
            _id: Number(id),
            startWaypointID: Number(startWaypointID),
            endWaypointID: Number(endWaypointID),
            distant: Number(distant),
            time: Number(time),
        })

        await mlways.update({"_id": Number(id)},
                            {
                                startWaypointID: Number(startWaypointID),
                                endWaypointID: Number(endWaypointID),
                                distant: Number(distant),
                                time: Number(time),
                            }, 
                            function(err) {
                                if (err) {
                                    mlway.save()
                                }
                            })
    }

    //await createMap();
    res.json(req.body)
})

router.post('/nearby', async (req,res) => {
    var startLat = Number(req.body.start_lat)
    var startLng = Number(req.body.start_lng)

    nearestPoint(startLat,startLng).then((newid)=>{
        console.log(newid);
    });

    //console.log(point)
    res.json({})
   
})

router.post('/datawaypoint', async (req,res) => {
    //1	21.027233, 105.787644	"Unnamed RoadCầu Giấy, Hà Nội, Việt Nam"
    let all = String(req.body.all)
    let line = all.split('\"\n')

    for (var i = 0; i < line.length; i++) {
        let each = line[i].split('\t')
        let id = each[0]
        let place = each[2].replace('\"','')
        let point = each[1].split(',')
        let lat = point[0].replace('\"','')
        let lng = point[1].replace('\"','')

        await new Waypoints({
                _id: Number(id),
                place: String(place),
                lat: Number(lat),
                lng: Number(lng)
            }).save()
    }
    
    console.log(line)

    res.json(req.body)
})

module.exports= router;

// 1	21.027233, 105.787644	"Unnamed Road
// Cầu Giấy, Hà Nội, Việt Nam"
// 3	21.024640, 105.789153	"Shop dầu tràm
// D29 Phạm Văn Bạch, Yên Hoà,"
// 2	21.026233, 105.788228	"Unnamed Road
// Yên Hoà, Cầu Giấy, Hà Nội, Việt Na"
// 4	21.023266, 105.790010	"Dương Đình Nghệ
// Yên Hoà, Cầu Giấy, Hà Nội, Việt Na"
// 5	21.021621, 105.791132	"Công Ty Cổ Phần NaBo Việt Nam
// 210 Trung Kính, Cầu Giấy, Yên Hoà"