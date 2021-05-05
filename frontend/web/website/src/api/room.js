import request from '../utils/request'

export function getAll(){
    return request({
        url: '/lab/room/fetch',
        method: 'GET'
    })
}

export function getAvailableInRoom(date, startTime, endTime){
    return request({
        url: '/lab/room/fetchAvailableInRoom',
        method: 'GET',
        params: {
            date: date,
            startTime: startTime,
            endTime: endTime
        }
    })
}