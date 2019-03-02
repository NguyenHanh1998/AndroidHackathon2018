# Project Title
AVA

Ava is an application used to find the way, assess the traffic density.
(Can predict the ability to move on road in a few minutes nearby).
# Active Flow
Machine learning: Evaluate indicators of traffic density, traffic flows and speed of vehicles on road by identifying on the video and then send data to server to process.

Server: process data received from host, send the fastest road to Client.

Client: receive data sent from server, draw and display the road.
# Pre-requisites
. Android SDK v27

. Latest Android Build Tools

. Android Support Repository

. Google Repository

. Google Play services

. Create Firebase app
# Getting Start
. Get an API key for the Google Maps API for Android

. Copy your google-services.json into /app/

. Paste your API key into app/src/debug/res/values/google_maps_api.xml
# Real Result run in Android app:


# Server side 
Using Nodejs v10.11.0

Get messages from the camera , using Dijkstra's algorithm to find the shortest path between 2 locations required 

# DATABASE
Using Mongodb

Connecting with Mlab

# Installation
    
    git clone https://github.com/NguyenHanh1998/AndroidHackathon2018.git
    cd AndroidHackathon2018/backend
    npm install

# Quick start
    
Run your app from the command line with:

    node index.js

**Connect mongodb**
    
    mongoose.set('useFindAndModify', false)
    mongoose.connect('mongodb://vinhpro1998:1998vinhpro@ds125362.mlab.com:25362/vinhnodb',{ useNewUrlParser: true });

**Find the shortest way**
    
    router.get('/local',(req,res)=>{

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


**Update data from camera**

    router.post('/maytram', async (req,res)=>{

        let all = String(req.body.all)
        let line = all.split('\n')

        for (var i = 0; i < line.length; i++) {
            let each = line[i].split('\t')
            let id = each[0]
            let startWaypointID = each[1]
            let endWaypointID = each[2]
            let distant = each[3]
            let time = each[4]

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

        res.json(req.body)
    })

