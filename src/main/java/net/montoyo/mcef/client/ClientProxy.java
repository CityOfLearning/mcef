package net.montoyo.mcef.client;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.OS;
import org.cef.browser.CefBrowserOsr;
import org.cef.browser.CefMessageRouter;
import org.cef.browser.CefMessageRouter.CefMessageRouterConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.montoyo.mcef.BaseProxy;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IDisplayHandler;
import net.montoyo.mcef.api.IJSQueryHandler;
import net.montoyo.mcef.example.ExampleMod;
import net.montoyo.mcef.remote.RemoteConfig;
import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.virtual.VirtualBrowser;

public class ClientProxy extends BaseProxy {

	public static String ROOT = ".";
	public static String APP_PATH = ".";
	public static String RESOURCE_PATH = ".";
	public static boolean VIRTUAL = false;

	private CefApp cefApp;
	private CefClient cefClient;
	private CefMessageRouter cefRouter;
	private boolean firstRouter = true;
	private ArrayList<CefBrowserOsr> browsers = new ArrayList<CefBrowserOsr>();
	private String updateStr;
	private Minecraft mc = Minecraft.getMinecraft();

	@Override
	public IBrowser createBrowser(String url, boolean transp) {
		if (VIRTUAL) {
			return new VirtualBrowser();
		}

		CefBrowserOsr ret = (CefBrowserOsr) cefClient.createBrowser(url, true, transp);
		browsers.add(ret);
		return ret;
	}

	public CefApp getCefApp() {
		return cefApp;
	}

	@Override
	public boolean isVirtual() {
		return VIRTUAL;
	}

	@Override
	public void onInit() {
		File f = new File(mc.mcDataDir.getParentFile(), "mcef");
		f.mkdirs();
		ROOT = f.getAbsolutePath().replaceAll("\\\\", "/");
		if(!addLibraryPaths(ROOT))
			return;
		if(OS.isMacintosh()){
			f = new File(mc.mcDataDir.getParentFile(), "mcef/jcef_app.app/Contents/Java");
			APP_PATH = f.getAbsolutePath().replaceAll("\\\\", "/");
			if(!addLibraryPaths(APP_PATH))
				return;
			f = new File(mc.mcDataDir.getParentFile(), " mcef/jcef_app.app/Contents/Frameworks/Chromium Embedded Framework.framework/Resources");
//			f = new File(mc.mcDataDir.getParentFile(), "mcef/Chromium Embedded Framework.framework/Resources");
			RESOURCE_PATH = f.getAbsolutePath().replaceAll("\\\\", "/");
			if(!addLibraryPaths(RESOURCE_PATH))
				return;
		}
		
		UpdateFrame uf = new UpdateFrame();
		RemoteConfig cfg = new RemoteConfig();

		cfg.load();
		if (!cfg.downloadMissing(uf)) {
			Log.warning("Going in virtual mode; couldn't download resources.");
			VIRTUAL = true;
			return;
		}

		updateStr = cfg.getUpdateString();
		uf.dispose();

		if (VIRTUAL) {
			return;
		}

		Log.info("Done without errors.");

		CefSettings settings = new CefSettings();
		settings.windowless_rendering_enabled = true;
		settings.background_color = settings.new ColorType(0, 255, 255, 255);
		if(OS.isWindows()){
			settings.locales_dir_path = (new File(ROOT, "locales")).getAbsolutePath();
		} 
		if(OS.isMacintosh()){
//			settings.resources_dir_path = RESOURCE_PATH;
//			settings.locales_dir_path = RESOURCE_PATH;
		}
		
		settings.cache_path = (new File(ROOT, "MCEFCache")).getAbsolutePath();
		 settings.log_severity = CefSettings.LogSeverity.LOGSEVERITY_VERBOSE;

		try {
			cefApp = CefApp.getInstance(settings);
			if(!OS.isMacintosh()){
				cefApp.myLoc = ROOT.replace('/', File.separatorChar);
			} else {
//				cefApp.myLoc = ROOT.replace('/', File.separatorChar);
				cefApp.myLoc = APP_PATH.replace('/', File.separatorChar);
			}
			CefApp.addAppHandler(new AppHandler());
			cefClient = cefApp.createClient();
		} catch (Throwable t) {
			Log.error("Going in virtual mode; couldn't initialize CEF.");
			t.printStackTrace();

			VIRTUAL = true;
			return;
		}

		Log.info(cefApp.getVersion().toString());
		cefRouter = CefMessageRouter.create(new CefMessageRouterConfig("mcefQuery", "mcefCancel"));
		cefClient.addMessageRouter(cefRouter);

		(new ShutdownThread(browsers)).start();
		MinecraftForge.EVENT_BUS.register(this);

		if (MCEF.ENABLE_EXAMPLE) {
			(new ExampleMod()).onInit();
		}

		Log.info("MCEF loaded successfuly.");
	}

	@SubscribeEvent
	public void onLogin(PlayerEvent.PlayerLoggedInEvent ev) {
		if ((updateStr == null) || !MCEF.WARN_UPDATES) {
			return;
		}

		ChatStyle cs = new ChatStyle();
		cs.setColor(EnumChatFormatting.LIGHT_PURPLE);

		ChatComponentText cct = new ChatComponentText(updateStr);
		cct.setChatStyle(cs);

		ev.player.addChatComponentMessage(cct);
	}

	@SubscribeEvent
	public void onTick(TickEvent.RenderTickEvent ev) {
		if (ev.phase == TickEvent.Phase.START) {
			mc.mcProfiler.startSection("MCEF");

			for (CefBrowserOsr b : browsers) {
				b.mcefUpdate();
			}

			mc.mcProfiler.endSection();
		}
	}

	@Override
	public void openExampleBrowser(String url) {
		if (MCEF.ENABLE_EXAMPLE) {
			ExampleMod.INSTANCE.showScreen(url);
		}
	}

	@Override
	public void registerDisplayHandler(IDisplayHandler idh) {
		if (!VIRTUAL) {
			cefClient.addDisplayHandler(new DisplayHandler(idh));
		}
	}

	@Override
	public void registerJSQueryHandler(IJSQueryHandler iqh) {
		if (!VIRTUAL) {
			cefRouter.addHandler(new MessageRouter(iqh), firstRouter); // SwingUtilities.invokeLater()
																		// ?
		}

		if (firstRouter) {
			firstRouter = false;
		}
	}

	public void removeBrowser(CefBrowserOsr b) {
		browsers.remove(b);
	}

	private boolean addLibraryPaths(String path){
		Log.info("Now adding \"%s\" to java.library.path", path);

		try {
				Field pathsField = ClassLoader.class.getDeclaredField("usr_paths");
				pathsField.setAccessible(true);

				String[] paths = (String[]) pathsField.get(null);
				String[] newList = new String[paths.length + 1];

				System.arraycopy(paths, 0, newList, 0, paths.length);
				newList[paths.length] = path.replace('/', File.separatorChar);
				pathsField.set(null, newList);
		} catch (Exception e) {
			Log.error("Failed to do it! Entering virtual mode...");
			e.printStackTrace();

			VIRTUAL = true;
			return false;
		}
		return true;
	}
}
