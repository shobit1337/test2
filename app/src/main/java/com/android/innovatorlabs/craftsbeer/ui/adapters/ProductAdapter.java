package com.android.innovatorlabs.craftsbeer.ui.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.innovatorlabs.craftsbeer.R;
import com.android.innovatorlabs.craftsbeer.constants.IEventConstants;
import com.android.innovatorlabs.craftsbeer.databinding.ProductItemBinding;
import com.android.innovatorlabs.craftsbeer.db.entity.ProductEntity;
import com.android.innovatorlabs.craftsbeer.model.Product;
import com.android.innovatorlabs.craftsbeer.utils.CommonUtils;

import java.util.List;
import java.util.Objects;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductEntity> mProducts;

    private Context context;

    private ProductItemBinding productItemBinding;

    private TypedArray typedArray;

    public ProductAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        typedArray = context.getResources().obtainTypedArray(R.array.images_array);

        productItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product_item, parent, false);

        return new ProductViewHolder(productItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        holder.productItemBinding.setProduct(mProducts.get(position));

        holder.productItemBinding.executePendingBindings();

        if(holder.productItemBinding.productImage.getBackground() == null){
            holder.productItemBinding.productImage.setBackgroundResource(typedArray.getResourceId(position%6, 0));
        }
    }

    @Override
    public int getItemCount() {
        return mProducts != null ? mProducts.size() : 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ProductItemBinding productItemBinding;

        public ProductViewHolder(ProductItemBinding productItemBinding) {

            super(productItemBinding.getRoot());

            this.productItemBinding = productItemBinding;

            productItemBinding.addToCart.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == productItemBinding.addToCart.getId()){

                CommonUtils.postEvent(IEventConstants.EVENT_PRODUCT_ADD_TO_CART, mProducts.get(getAdapterPosition()));
            }
        }
    }

    public void addProducts(final List<ProductEntity> products){

        if(mProducts == null || mProducts.isEmpty()){

            mProducts = products;

            if(mProducts != null){
                notifyItemRangeInserted(0, mProducts.size());
            }

        }else {

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mProducts.size();
                }

                @Override
                public int getNewListSize() {
                    return products.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mProducts.get(oldItemPosition).getId() ==
                            products.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Product newProduct = products.get(newItemPosition);
                    Product oldProduct = mProducts.get(oldItemPosition);
                    return newProduct.getId() == oldProduct.getId()
                            && Objects.equals(newProduct.getAlcoholContent(), oldProduct.getAlcoholContent())
                            && Objects.equals(newProduct.getBitterUnits(), oldProduct.getBitterUnits())
                            && Objects.equals(newProduct.getOunces(), oldProduct.getOunces())
                            && Objects.equals(newProduct.getStyle(), oldProduct.getStyle());
                }
            });
            mProducts = products;
            result.dispatchUpdatesTo(this);
        }
    }

    public void setProducts(List<ProductEntity> products){
        this.mProducts = products;
    }

    public List<ProductEntity> getProducts(){
        return mProducts;
    }
}
