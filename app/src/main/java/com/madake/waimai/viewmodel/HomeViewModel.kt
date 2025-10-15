import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madake.waimai.db.WaimaiDatabase
import com.madake.waimai.db.dao.GoodsDao
import com.madake.waimai.db.entity.GoodsEntity
import kotlinx.coroutines.launch

class HomeViewModel(context: Context) : ViewModel() {

    private val goodsDao = WaimaiDatabase.getInstance(context).goodsDao()

    private val pageSize = 10

    // 百亿补贴商品分页
    private var billionPage = 0
    private val _billionGoods = MutableLiveData<List<GoodsEntity>>()
    val billionGoods: LiveData<List<GoodsEntity>> = _billionGoods

    // 销量商品分页
    private var hotPage = 0
    private val _hotGoods = MutableLiveData<List<GoodsEntity>>()
    val hotGoods: LiveData<List<GoodsEntity>> = _hotGoods

    // 销量商品分页
    private var allPage = 0
    private val _allGoods = MutableLiveData<List<GoodsEntity>>()
    val allGoods: LiveData<List<GoodsEntity>> = _allGoods

    // 加载下一页百亿补贴商品
    fun loadNextBillionGoods() {
        viewModelScope.launch {
            val items = goodsDao.getBillionSubsidyGoods(offset = billionPage * pageSize, limit = pageSize)
            if (items.isNotEmpty()) {
                val current = _billionGoods.value?.toMutableList() ?: mutableListOf()
                current.addAll(items)
                _billionGoods.value = current
                billionPage++
            }
        }
    }

    // 加载下一页销量商品
    fun loadNextHotGoods() {
        viewModelScope.launch {
            val items = goodsDao.getHotGoods(offset = hotPage * pageSize, limit = pageSize)
            if (items.isNotEmpty()) {
                val current = _hotGoods.value?.toMutableList() ?: mutableListOf()
                current.addAll(items)
                _hotGoods.value = current
                hotPage++
            }
        }
    }

    // 加载下一页销量商品
    fun loadNextAllGoods() {
        viewModelScope.launch {
            val items = goodsDao.getAllGoods(offset = allPage * pageSize, limit = pageSize)
            if (items.isNotEmpty()) {
                val current = _allGoods.value?.toMutableList() ?: mutableListOf()
                current.addAll(items)
                _allGoods.value = current
                allPage++
            }
        }
    }
}
