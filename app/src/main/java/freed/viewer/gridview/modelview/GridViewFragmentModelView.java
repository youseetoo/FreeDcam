package freed.viewer.gridview.modelview;

import android.app.AlertDialog;
import android.app.RecoverableSecurityException;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;

import androidx.lifecycle.ViewModel;

import com.troop.freedcam.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import freed.ActivityAbstract;
import freed.file.FileListController;
import freed.file.holder.BaseHolder;
import freed.file.holder.FileHolder;
import freed.file.holder.UriHolder;
import freed.image.ImageManager;
import freed.utils.FreeDPool;
import freed.utils.Log;
import freed.utils.StringUtils;
import freed.viewer.dngconvert.DngConvertingActivity;
import freed.viewer.dngconvert.DngConvertingFragment;
import freed.viewer.gridview.enums.RequestModes;
import freed.viewer.gridview.enums.ViewStates;
import freed.viewer.gridview.models.ButtonDoAction;
import freed.viewer.gridview.models.FilesHolderModel;
import freed.viewer.gridview.models.FilesSelectedModel;
import freed.viewer.gridview.models.FinishActivityModel;
import freed.viewer.gridview.models.IntentModel;
import freed.viewer.gridview.models.ViewStateModel;
import freed.viewer.gridview.models.VisibilityButton;
import freed.viewer.stack.DngStackActivity;
import freed.viewer.stack.StackActivity;

public class GridViewFragmentModelView extends ViewModel
{
    private final String TAG = GridViewFragmentModelView.class.getSimpleName();
    private ViewStateModel viewStateModel;
    private FilesHolderModel filesHolderModel;
    private boolean isRootDir;
    private List<BaseHolder> filesSelectedList = new ArrayList<>();
    private List<UriHolder> urisToDelte = new ArrayList<>();
    private VisibilityButton buttonFiletype;
    private ButtonDoAction buttonDoAction;
    private VisibilityButton buttonOptions;
    private VisibilityButton textViewFilesSelected;

    public FileListController.FormatTypes formatsToShow = FileListController.FormatTypes.all;
    private FileListController.FormatTypes lastFormat = FileListController.FormatTypes.all;
    private RequestModes requestMode = RequestModes.none;
    private BaseHolder folderToShow;
    private IntentModel intentModel;
    private FinishActivityModel finishActivityModel;
    private FinishActivityModel alterDialogModel;
    private FilesSelectedModel filesSelectedModel;

    public GridViewFragmentModelView(FileListController fileListController)
    {
        viewStateModel = new ViewStateModel();
        filesHolderModel = new FilesHolderModel(fileListController);
        buttonFiletype = new VisibilityButton();
        buttonDoAction = new ButtonDoAction();
        buttonOptions = new VisibilityButton();
        textViewFilesSelected = new VisibilityButton();
        intentModel = new IntentModel();
        finishActivityModel = new FinishActivityModel();
        alterDialogModel = new FinishActivityModel();
        filesSelectedModel = new FilesSelectedModel();
    }

    public ViewStateModel getViewStateModel() {
        return viewStateModel;
    }

    public FilesHolderModel getFilesHolderModel() {
        return filesHolderModel;
    }

    public IntentModel getIntentModel() {
        return intentModel;
    }

    public FinishActivityModel getFinishActivityModel() {
        return finishActivityModel;
    }

    public FinishActivityModel getAlterDialogModel() {
        return alterDialogModel;
    }

    public boolean isRootDir() {
        return isRootDir;
    }

    public void setViewMode(ViewStates viewState)
    {
        Log.d(TAG,"setViewMode:  isRootDir" + isRootDir);
        viewStateModel.setCurrentViewState(viewState);
        if (isRootDir)
        {
            buttonFiletype.setVisibility(false);
            textViewFilesSelected.setVisibility(false);
        }
        else
        {
            switch (viewState)
            {
                case normal:
                    if (formatsToShow == FileListController.FormatTypes.raw && lastFormat != FileListController.FormatTypes.raw) {
                        formatsToShow = lastFormat;
                        filesHolderModel.LoadFolder(folderToShow,formatsToShow);
                    }
                    //resetFilesSelected();
                    requestMode = RequestModes.none;
                    buttonFiletype.setVisibility(true);
                    textViewFilesSelected.setVisibility(false);
                    buttonOptions.setVisibility(true);
                    buttonDoAction.setVisibility(false);
                    break;
                case selection:
                    resetFilesSelected();
                    textViewFilesSelected.setVisibility(true);
                    updateFilesSelected();
                    switch (requestMode) {
                        case none:
                            buttonFiletype.setVisibility(true);
                            buttonOptions.setVisibility(true);
                            buttonDoAction.setVisibility(false);
                            buttonDoAction.setOnClickListener(null);
                            break;
                        case delete:
                            buttonFiletype.setVisibility(false);
                            buttonOptions.setVisibility(false);
                            buttonDoAction.setText("Delete");
                            buttonDoAction.setOnClickListener(onDeltedButtonClick);
                            buttonDoAction.setVisibility(true);
                            break;
                        case rawToDng:
                            lastFormat = formatsToShow;
                            formatsToShow = FileListController.FormatTypes.raw;
                            filesHolderModel.LoadFolder(folderToShow,formatsToShow);
                            buttonOptions.setVisibility(false);
                            buttonFiletype.setVisibility(false);
                            buttonDoAction.setText("RawToDng");
                            buttonDoAction.setOnClickListener(onRawToDngClick);
                            buttonDoAction.setVisibility(true);
                            break;
                        case stack:
                            lastFormat = formatsToShow;
                            formatsToShow = FileListController.FormatTypes.jpg;
                            filesHolderModel.LoadFolder(folderToShow,formatsToShow);
                            buttonOptions.setVisibility(false);
                            buttonFiletype.setVisibility(false);
                            buttonDoAction.setText("Stack");
                            buttonDoAction.setOnClickListener(onStackClick);
                            buttonDoAction.setVisibility(true);
                            break;
                        case dngstack:
                            lastFormat = formatsToShow;
                            formatsToShow = FileListController.FormatTypes.dng;
                            filesHolderModel.LoadFolder(folderToShow,formatsToShow);
                            buttonOptions.setVisibility(false);
                            buttonFiletype.setVisibility(false);
                            buttonDoAction.setText("DngStack");
                            buttonDoAction.setOnClickListener(onDngStackClick);
                            buttonDoAction.setVisibility(true);
                            break;
                    }
                    break;
            }
        }
    }

    public final View.OnClickListener onStackClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (requestMode == RequestModes.none)
            {
                requestMode = RequestModes.stack;
                setViewMode(ViewStates.selection);
            }
            else if (requestMode == RequestModes.stack)
            {
                ArrayList<String> ar = new ArrayList<>();
                for (BaseHolder f : filesHolderModel.getFiles()) {
                    if (f.IsSelected() && f.getName().toLowerCase().endsWith(StringUtils.FileEnding.JPG))
                    {
                        if (f instanceof FileHolder)
                            ar.add(((FileHolder)f).getFile().getAbsolutePath());
                        else if (f instanceof UriHolder)
                            ar.add(((UriHolder)f).getMediaStoreUri().toString());
                    }

                }
                for (BaseHolder f : filesHolderModel.getFiles()) {
                    f.SetSelected(false);
                }
                setViewMode(ViewStates.normal);
                intentModel.setAr(ar);
                intentModel.setIntentClass(StackActivity.class);
            }
            else
            {
                requestMode = RequestModes.none;
                setViewMode(ViewStates.normal);
            }
        }
    };

    public final View.OnClickListener onDngStackClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (requestMode == RequestModes.none)
            {
                requestMode = RequestModes.dngstack;
                setViewMode(ViewStates.selection);
            }
            else if (requestMode == RequestModes.dngstack)
            {
                ArrayList<String> ar = new ArrayList<>();
                for (BaseHolder f : getFilesHolderModel().getFiles()) {
                    if (f.IsSelected() && f.getName().toLowerCase().endsWith(StringUtils.FileEnding.DNG))
                    {
                        if (f instanceof FileHolder)
                            ar.add(((FileHolder)f).getFile().getAbsolutePath());
                        else if (f instanceof UriHolder)
                            ar.add(((UriHolder)f).getMediaStoreUri().toString());
                    }

                }
                for (BaseHolder f : getFilesHolderModel().getFiles()) {
                    f.SetSelected(false);
                }
                setViewMode(ViewStates.normal);
                intentModel.setAr(ar);
                intentModel.setIntentClass(DngStackActivity.class);
            }
            else
            {
                requestMode = RequestModes.none;
                setViewMode(ViewStates.normal);
            }
        }
    };

    public final View.OnClickListener onGobBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            if (viewStateModel.getCurrentViewState() == ViewStates.normal)
            {
                if (getFilesHolderModel().getFiles() != null && getFilesHolderModel().getFiles().size() > 0
                        && getFilesHolderModel().getFiles().get(0) instanceof FileHolder)
                {
                    FileHolder fileHolder = (FileHolder) getFilesHolderModel().getFiles().get(0);
                    File topPath = fileHolder.getFile().getParentFile().getParentFile();
                    if (topPath.getName().equals("DCIM") && !isRootDir)
                    {
                        getFilesHolderModel().loadDefault();
                        isRootDir = true;
                        Log.d(TAG, "onGobBackClick dcim folder rootdir:" +isRootDir);
                        setViewMode(viewStateModel.getCurrentViewState());
                    }
                    else if (isRootDir)
                    {
                        finishActivityModel.setOb(null);
                    }
                    else
                    {
                        isRootDir = false;
                        Log.d(TAG, "onGobBackClick load folder rootdir:" +isRootDir);
                        filesHolderModel.loadDefault();
                        //viewerActivityInterface.LoadFolder(viewerActivityInterface.getFiles().get(0),formatsToShow);
                        setViewMode(viewStateModel.getCurrentViewState());
                    }
                }
                else if (filesHolderModel.getFiles().size() > 0 && filesHolderModel.getFiles().get(0) instanceof UriHolder)
                    if (filesHolderModel.getFiles().get(0).IsFolder())
                        finishActivityModel.setOb(null);
                    else
                        filesHolderModel.loadDefault();
                else
                {
                    filesHolderModel.loadDefault();
                    //viewerActivityInterface.LoadDCIMDirs();
                    Log.d(TAG, "onGobBackClick dcim folder rootdir:" +isRootDir);
                    isRootDir = true;

                    setViewMode(viewStateModel.getCurrentViewState());
                    if (filesHolderModel.getFiles().size() == 0)
                        finishActivityModel.setOb(null);
                }
            }
            else if (viewStateModel.getCurrentViewState() == ViewStates.selection)
            {
                for (int i = 0; i< filesHolderModel.getFiles().size(); i++)
                {
                    BaseHolder f = filesHolderModel.getFiles().get(i);
                    f.SetSelected(false);
                }
                setViewMode(ViewStates.normal);
            }
        }
    };

    public final View.OnClickListener onRawToDngClick = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            if (requestMode == RequestModes.none)
            {
                requestMode = RequestModes.rawToDng;
                setViewMode(ViewStates.selection);
            }
            else if (requestMode == RequestModes.rawToDng)
            {
                ArrayList<String> ar = new ArrayList<>();
                for (BaseHolder f : getFilesHolderModel().getFiles()) {
                    if (f.IsSelected() &&
                            (f.getName().toLowerCase().endsWith(StringUtils.FileEnding.RAW) ||f.getName().toLowerCase().endsWith(StringUtils.FileEnding.BAYER))) {
                        if (f instanceof FileHolder)
                            ar.add(((FileHolder) f).getFile().getAbsolutePath());
                        else if (f instanceof UriHolder)
                            ar.add(((UriHolder) f).getMediaStoreUri().toString());
                    }

                }
                for (BaseHolder f : getFilesHolderModel().getFiles()) {
                    f.SetSelected(false);
                }
                setViewMode(ViewStates.normal);
                intentModel.setAr(ar);
                intentModel.setIntentClass(DngConvertingActivity.class);
            }
            else
            {
                requestMode = RequestModes.none;
                setViewMode(ViewStates.normal);
            }
        }
    };

    public final View.OnClickListener onDeltedButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            if (requestMode == RequestModes.none)
            {
                requestMode = RequestModes.delete;
                setViewMode(ViewStates.selection);
            }
            else if (requestMode == RequestModes.delete)
            {
                //check if files are selected
                boolean hasfilesSelected = false;
                for (BaseHolder f : getFilesHolderModel().getFiles()) {
                    if (f.IsSelected()) {
                        hasfilesSelected = true;
                        break;
                    }
                }
                //if no files selected skip dialog
                if (!hasfilesSelected)
                    return;
                //else show dialog
                alterDialogModel.setOb(null);
                setViewMode(ViewStates.normal);
            }
            else
            {
                requestMode = RequestModes.none;
                setViewMode(ViewStates.normal);
            }
        }
    };

    public void deleteFiles()
    {
        ImageManager.cancelImageLoadTasks();
        FreeDPool.Execute(() -> {
            urisToDelte.clear();
            for (int i = 0; i < filesSelectedList.size(); i++)
            {
                try {
                    filesHolderModel.deleteFile(filesSelectedList.get(i));
                }
                catch(SecurityException ex){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (ex instanceof RecoverableSecurityException)
                        {
                            RecoverableSecurityException rex = (RecoverableSecurityException)ex;
                            try {
                                UriHolder uriHolder = (UriHolder) filesSelectedList.get(i);
                                urisToDelte.add(uriHolder);
                                startIntentSenderForResult(rex.getUserAction().getActionIntent().getIntentSender(), ActivityAbstract.DELETE_REQUEST_CODE,null,0,0,0,null);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            filesHolderModel.loadDefault();
        });
    }

    private void resetFilesSelected()
    {
        for (int i = 0; i< filesSelectedList.size(); i++)
        {
            BaseHolder f = filesSelectedList.get(i);
            f.SetSelected(false);
        }
        filesSelectedList.clear();
    }

    private void updateFilesSelected()
    {
        filesSelectedModel.setFilesSelectedCount(filesSelectedList.size());
    }

    public final PopupMenu.OnMenuItemClickListener popupMenuItemClickListner = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.all)
            {
                buttonFiletype.setText(R.string.ALL);
                formatsToShow = FileListController.FormatTypes.all;
            }
            else if (i == R.id.raw)
            {
                buttonFiletype.setText("RAW");
                formatsToShow = FileListController.FormatTypes.raw;
            }
            else if (i == R.id.bayer)
            {
                buttonFiletype.setText("BAYER");
                formatsToShow = FileListController.FormatTypes.raw;
            }
            else if (i == R.id.dng)
            {
                buttonFiletype.setText("DNG");
                formatsToShow = FileListController.FormatTypes.dng;
            }
            else if (i == R.id.jps)
            {
                buttonFiletype.setText("JPS");
                formatsToShow = FileListController.FormatTypes.jps;
            }
            else if (i == R.id.jpg)
            {
                buttonFiletype.setText("JPG");
                formatsToShow = FileListController.FormatTypes.jpg;
            }
            else if (i == R.id.mp4)
            {
                buttonFiletype.setText("MP4");
                formatsToShow = FileListController.FormatTypes.mp4;
            }
            //if (savedInstanceFilePath != null)
            filesHolderModel.LoadFolder(folderToShow,formatsToShow);

            return false;
        }
    };
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        switch (viewStateModel.getCurrentViewState())
        {
            case normal:
                //handel normal griditem click to open screenslide when its not a folder
                if (!filesHolderModel.getFiles().get(position).IsFolder())
                {
                    this.onGridItemClick.onButtonClick(position, view);
                }
                else //handel folder click
                {
                    //hold the current folder to show if a format is empty
                    folderToShow = filesHolderModel.getFiles().get(position);
                    filesHolderModel.LoadFolder(folderToShow,formatsToShow);
                    isRootDir = false;
                    setViewMode(viewStateModel.getCurrentViewState());

                }
                break;
            case selection:
                if (filesHolderModel.getFiles().get(position).IsSelected()) {
                    filesHolderModel.getFiles().get(position).SetSelected(false);
                    filesSelectedList.remove(filesHolderModel.getFiles().get(position));
                } else {
                    filesHolderModel.getFiles().get(position).SetSelected(true);
                    filesSelectedList.add(filesHolderModel.getFiles().get(position));
                }
                updateFilesSelected();
                mPagerAdapter.setViewState(currentViewState,position);
                break;
        }
    }
}
