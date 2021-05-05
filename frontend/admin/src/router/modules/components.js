/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'
import {getRole} from '@/utils/auth'

function checkHidenComponent(){
  let role = getRole()
  if(role === 'STAFF' || role === 'ADMIN'){
    return false;
  }
  return true;
}

const componentsRouter = {
  path: '/components',
  component: Layout,
  redirect: 'noRedirect',
  name: 'Component',
  hidden: checkHidenComponent(),
  meta: {
    title: 'Category',
    icon: 'component'
  },
  children: [
    {
      path: 'room',
      component: () => import('@/views/categories/Room'),
      name: 'Room',
      meta: { title: 'Room' }
    },
    {
      path: 'equiqment',
      component: () => import('@/views/categories/Room'),
      name: 'Equiqment',
      meta: { title: 'Equiqment' }
    }
  ]
}

export default componentsRouter
