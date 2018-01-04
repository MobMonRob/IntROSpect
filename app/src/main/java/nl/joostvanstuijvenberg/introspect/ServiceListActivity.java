package nl.joostvanstuijvenberg.introspect;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.data.Service;
import nl.joostvanstuijvenberg.introspect.viewmodel.ServiceViewModel;

/**
 * Created by Joost van Stuijvenberg on 21/10/2017.
 *
 * This file is part of IntROSpect. IntROSpect is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * IntROSpect is distributed in the hope that it will be useful but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with IntROSpect.  If
 * not, see <http://www.gnu.org/licenses/>.
 */

public class ServiceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Master mMaster;
    private ArrayList<String> mItems;
    private ArrayAdapter<String> mAdapter;
    private ListView mServiceListView;
    private ServiceViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMaster = getIntent().getExtras().getParcelable(Constants.PARCEL_KEY_MASTER);
        mViewModel = ViewModelProviders.of(this, new ServiceViewModel.Factory(mMaster, null)).get(ServiceViewModel.class);

        // Inflate the user interface.
        setContentView(R.layout.activity_service_list);
        mItems = new ArrayList<>();
        mAdapter = new ArrayAdapter<String>(this, R.layout.activity_service_list_row, mItems);
        mServiceListView = (ListView)findViewById(R.id.serviceListView);
        mServiceListView.setAdapter(mAdapter);
        mServiceListView.setOnItemClickListener(this);

        // Observe the ViewModel's service list and make sure changes are reflected in the user interface.
        mViewModel.serviceList.observe(this, new Observer<ServiceViewModel.ServiceListViewModel>() {
            @Override
            public void onChanged(@Nullable ServiceViewModel.ServiceListViewModel serviceListViewModel) {
                if (serviceListViewModel != null) {
                    mItems.clear();
                    for (Service s : serviceListViewModel.services)
                        mItems.add(s.name);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String serviceName = (String)adapterView.getItemAtPosition(position);
        Intent intent = new Intent(getApplicationContext(), ServiceDetailsActivity.class);
        intent.putExtra(Constants.PARCEL_KEY_MASTER, mMaster);
        intent.putExtra(Constants.PARCEL_KEY_SERVICE, serviceName);
        startActivity(intent);
    }

}
