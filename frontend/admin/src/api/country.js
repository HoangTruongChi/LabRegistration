import request from '@/utils/request'

export function fetchCountries(query) {
    return request({
      url: '/api/v1/components/countries',
      method: 'get',
      params: query
    })
  }

  export function deleteCountry(countryId, deleted){
    return request({
      url:'/api/v1/components/countries',
      method: 'delete',
      params: {
        countryId: countryId,
        deleted: deleted
      }
    })
  }

  export function createCountry(data) {
    return request({
      url: '/api/v1/components/countries',
      method: 'post',
      data
    })
  }

  export function updateCountry(data) {
    return request({
      url: '/api/v1/components/countries',
      method: 'put',
      data
    })
  }
