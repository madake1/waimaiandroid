import android.util.Log
import com.madake.waimai.Bean.LoginResult
import com.madake.waimai.Bean.RegisterResult
import com.madake.waimai.Bean.UserDTO
import com.madake.waimai.db.dao.UserDao
import com.madake.waimai.db.entity.UserEntity
import com.madake.waimai.db.WaimaiDatabase

import com.madake.waimai.mcontext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object UserRepository {

    // 直接用全局 context 获取 DAO
    private val userDao: UserDao by lazy {
        WaimaiDatabase.getInstance(mcontext).userDao()
    }

    suspend fun register(dto: UserDTO): RegisterResult {
        return try {
            Log.d("madake1", "UserDao = $userDao")
            // 先检查手机号是否已注册
            val existingUser = userDao.getUserByPhone(dto.phone)
            if (existingUser != null) {
                return RegisterResult(success = false, msg = "手机号已注册")
            }
            Log.d("madake1", "已经注册了吗 = $existingUser")

            // 构造实体类
            val entity = UserEntity(
                phone = dto.phone,
                password = dto.password,
                birthday = dto.birthday,
                nickName = dto.nickName,
                headImg = dto.headImg,
                gender = dto.gender,
                createTime = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )
            Log.d("madake1", "注册信息 = $entity")

            // 插入数据库
            userDao.insert(entity)

            Log.d("madake1", "插入完成")

            RegisterResult(success = true, msg = "注册成功")
        } catch (e: Exception) {
            e.printStackTrace()
            RegisterResult(success = false, msg = "未知错误")
        }
    }
    suspend fun clearUsers() {
        userDao.clearAllUsers()
    }

    suspend fun login(phone: String, password: String): LoginResult {
        return try {
            val user = userDao.getUserByPhone(phone)
            if (user == null) {
                LoginResult(success = false, msg = "该账号未注册")
            } else if (user.password != password) {
                LoginResult(success = false, msg = "密码不对")
            } else {
                LoginResult(success = true, msg = "登录成功", user = user)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoginResult(success = false, msg = "未知错误")
        }
    }

    suspend fun getUserIdByPhone(phone: String): Int? {
        return userDao.getUserByPhone(phone)?.id
    }


}
