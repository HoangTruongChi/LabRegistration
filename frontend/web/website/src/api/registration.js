import request from '../utils/request'

export function register(data){
    return request({
        url: '/lab/registration',
        method: 'post',
        data
    })
}

export function getAll(){
    return request({
        url: '/lab/registration',
        method: 'GET'
    })
}

export function getResult(id){
    return request({
        url: '/lab/result',
        method: 'GET',
        params: {id:id}
    })
}

export function cancel(id){
    return request({
        url: '/lab/registration',
        method: 'put',
        params: {id:id}
    })
}