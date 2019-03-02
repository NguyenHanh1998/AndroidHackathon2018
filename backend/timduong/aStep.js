async function stepJson (allPoint,source){  
    
    var path=source.path;
    var step=[];
    //console.log(allPoint[6].lat)
    for (var i = 1; i < path.length; i++) {

        let aStep = 
            {
                start_location: {
                    lat: await allPoint[path[i-1]].lat,
                    lng: await allPoint[path[i-1]].lng
                },
                end_location: {
                    lat: await allPoint[path[i]].lat,
                    lng: await allPoint[path[i]].lng
                }
            }
        step.push(aStep)

    }

    let responce = {
        Instruct: {
            step: step
        }
    }

    console.log(responce)
    return responce;
}

module.exports = stepJson;