// src/vuex/actions.js
import {USER_ID, IS_AUTH, ERROR_STATE, USER_IDX, IS_LOGIN} from './mutation_types'
import loginAPI from '../service/loginAPI'
import joinAPI from '../service/joinAPI'

let setUserId = ({commit}, data) => {
    commit(USER_ID, data)
}

let setErrorState = ({commit}, data) => {
    commit(ERROR_STATE, data)
}

let setIsAuth = ({commit}, data) => {
    commit(IS_AUTH, data)
}

let setUserIdx = ({commit}, data)=>{
    commit(USER_IDX,data)
}

let setIsLogin = ({commit}, data) => {
    commit(IS_LOGIN, data)
}

// 백엔드에서 반환한 결과값을 가지고 로그인 성공 실패 여부를 vuex에 넣어준다.
let processResponse = (store, loginResponse) => {

    switch (loginResponse) {
        case 'notFound':
            setErrorState(store, 'Wrong ID or Password')
            setIsAuth(store, false)
            break
        case 'withdrawal':
            setErrorState(store, 'Withdrawal ID')
            setIsAuth(store, false)
        default:
            setErrorState(store, '')
            setIsAuth(store, true)
            setIsLogin(store, true)
            //setUserIdx(store, loginResponse.user_idx)
            //setUserIdx(store, 5)
    }
}

let processResponse1 = (store, joinResponse) => {

    switch (joinResponse) {
        case 'notFound':
            setErrorState(store, 'false')
            setIsAuth(store, false)
            break

        default:
            setErrorState(store, '')
            setIsAuth(store, true)
    }
}

let updateLoginStateBySocial = (store) => {
    setErrorState(store, '')
    setIsAuth(store, true)
    setIsLogin(store, true)
}


export default {
    async login (store, {user_id, user_pw}) {
        let loginResponse = await loginAPI.doLogin(user_id, user_pw)
        setUserId(store,loginResponse.data.user_id)
        setUserIdx(store,loginResponse.data.user_idx)
        processResponse(store, loginResponse)
        return store.getters.getIsAuth  // 로그인 결과를 리턴한다
    },
    async socialLogin (store, {user_id, user_idx}) {
        setUserId(store,user_id)
        setUserIdx(store,user_idx)
        updateLoginStateBySocial(store)
        return store.getters.getIsAuth  // 로그인 결과를 리턴한다
    },

    async join (store, {user_id, user_pw, user_name, phone_num, zip_code,addr,detail_addr,email}) {
        let joinResponse = await joinAPI.doJoin(user_id, user_pw, user_name,phone_num, zip_code,addr,detail_addr,email)
        processResponse1(store,joinResponse)
        return store.getters.getIsAuth  // 회원가입 결과 리턴
    }
}