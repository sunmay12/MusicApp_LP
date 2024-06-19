package com.example.minseo;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileEditFragment extends Fragment {
    private ImageButton finishBtn;
    private ImageView editProfileImage;
    private EditText editNameText;
    private Uri selectedImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        editProfileImage = view.findViewById(R.id.edit_profile);
        editNameText = view.findViewById(R.id.uedit_name);
        finishBtn = view.findViewById(R.id.button_finish);

        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                activityResultLauncher.launch(intent);
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 프로필 정보 저장
                saveProfileInfo();

                // 프로필 정보 업데이트
                ProfileFragment profileFragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("userName", editNameText.getText().toString());
                if (selectedImageUri != null) {
                    bundle.putString("profileImageUri", selectedImageUri.toString());
                }
                profileFragment.setArguments(bundle);

                // profile_fragment로 전환
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.containers, profileFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void saveProfileInfo() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", editNameText.getText().toString());
        if (selectedImageUri != null) {
            editor.putString("profileImageUri", selectedImageUri.toString());
        }
        editor.apply();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            selectedImageUri = intent.getData();
                            editProfileImage.setImageURI(selectedImageUri);
                        }
                    }
                }
            }
    );
}
