import axios from '../utils/axios'

const getUserInfo = (userId, userPw) => {
    const reqData = {
        'user_id': userId,
        'user_pw': userPw
    }

    return axios.post('/user/login', reqData, {
        headers: {
            'Content-type': 'application/json'
        }
    })
}

export default {
    async doLogin(userId, userPw) {
        try {
            const getUserInfoPromise = getUserInfo(userId, userPw)
            const [userInfoResponse] = await Promise.all([getUserInfoPromise])

            if (userInfoResponse.data.length === 0) {
                return 'notFound'
            }else if(userInfoResponse.data.errorCode == 402){
                return 'withdrawal'
            } else {
                localStorage.setItem('user_token', userInfoResponse.data.user_token)
                localStorage.setItem('user_role', userInfoResponse.data.user_role)
                return userInfoResponse
            }
        } catch (err) {

        }
    }
}