package etgg;
import com.sun.jna.*;
import java.util.List;
import java.util.Arrays;
/* Contains code from SDL, which has this copyright:

Simple DirectMedia Layer
Copyright (C) 1997-2014 Sam Lantinga <slouken@libsdl.org>
  
This software is provided 'as-is', without any express or implied
warranty.  In no event will the authors be held liable for any damages
arising from the use of this software.

Permission is granted to anyone to use this software for any purpose,
including commercial applications, and to alter it and redistribute it
freely, subject to the following restrictions:
  
1. The origin of this software must not be misrepresented; you must not
   claim that you wrote the original software. If you use this software
   in a product, an acknowledgment in the product documentation would be
   appreciated but is not required. 
2. Altered source versions must be plainly marked as such, and must not be
   misrepresented as being the original software.
3. This notice may not be removed or altered from any source distribution.


*/

public class SDL {
    static {
        Native.register("SDL2");
    }

    // SDL_shape.h
    //     WindowShapeMode
    public static final int ShapeModeDefault = 0;
    public static final int ShapeModeBinarizeAlpha = ShapeModeDefault + 1;
    public static final int ShapeModeReverseBinarizeAlpha = ShapeModeBinarizeAlpha + 1;
    public static final int ShapeModeColorKey = ShapeModeReverseBinarizeAlpha + 1;
    // SDL_audio.h
    //     SDL_AudioStatus
    public static final int SDL_AUDIO_STOPPED  =  0;
    public static final int SDL_AUDIO_PLAYING = SDL_AUDIO_STOPPED  + 1;
    public static final int SDL_AUDIO_PAUSED = SDL_AUDIO_PLAYING + 1;
    // SDL_audio.h
    public static final int SDL_AUDIO_ALLOW_FREQUENCY_CHANGE = 0x00000001 ;
    public static final int SDL_AUDIO_ALLOW_FORMAT_CHANGE = 0x00000002 ;
    public static final int SDL_AUDIO_ALLOW_CHANNELS_CHANGE = 0x00000004 ;
    public static final int SDL_MIX_MAXVOLUME = 128 ;
    // SDL_events.h
    //     SDL_EventType
    public static final int SDL_FIRSTEVENT  =  0;
    public static final int SDL_QUIT  =  0x100;
    public static final int SDL_APP_TERMINATING = SDL_QUIT  + 1;
    public static final int SDL_APP_LOWMEMORY = SDL_APP_TERMINATING + 1;
    public static final int SDL_APP_WILLENTERBACKGROUND = SDL_APP_LOWMEMORY + 1;
    public static final int SDL_APP_DIDENTERBACKGROUND = SDL_APP_WILLENTERBACKGROUND + 1;
    public static final int SDL_APP_WILLENTERFOREGROUND = SDL_APP_DIDENTERBACKGROUND + 1;
    public static final int SDL_APP_DIDENTERFOREGROUND = SDL_APP_WILLENTERFOREGROUND + 1;
    public static final int SDL_WINDOWEVENT  =  0x200;
    public static final int SDL_SYSWMEVENT = SDL_WINDOWEVENT  + 1;
    public static final int SDL_KEYDOWN  =  0x300;
    public static final int SDL_KEYUP = SDL_KEYDOWN  + 1;
    public static final int SDL_TEXTEDITING = SDL_KEYUP + 1;
    public static final int SDL_TEXTINPUT = SDL_TEXTEDITING + 1;
    public static final int SDL_MOUSEMOTION  =  0x400;
    public static final int SDL_MOUSEBUTTONDOWN = SDL_MOUSEMOTION  + 1;
    public static final int SDL_MOUSEBUTTONUP = SDL_MOUSEBUTTONDOWN + 1;
    public static final int SDL_MOUSEWHEEL = SDL_MOUSEBUTTONUP + 1;
    public static final int SDL_JOYAXISMOTION  =  0x600;
    public static final int SDL_JOYBALLMOTION = SDL_JOYAXISMOTION  + 1;
    public static final int SDL_JOYHATMOTION = SDL_JOYBALLMOTION + 1;
    public static final int SDL_JOYBUTTONDOWN = SDL_JOYHATMOTION + 1;
    public static final int SDL_JOYBUTTONUP = SDL_JOYBUTTONDOWN + 1;
    public static final int SDL_JOYDEVICEADDED = SDL_JOYBUTTONUP + 1;
    public static final int SDL_JOYDEVICEREMOVED = SDL_JOYDEVICEADDED + 1;
    public static final int SDL_CONTROLLERAXISMOTION  =  0x650;
    public static final int SDL_CONTROLLERBUTTONDOWN = SDL_CONTROLLERAXISMOTION  + 1;
    public static final int SDL_CONTROLLERBUTTONUP = SDL_CONTROLLERBUTTONDOWN + 1;
    public static final int SDL_CONTROLLERDEVICEADDED = SDL_CONTROLLERBUTTONUP + 1;
    public static final int SDL_CONTROLLERDEVICEREMOVED = SDL_CONTROLLERDEVICEADDED + 1;
    public static final int SDL_CONTROLLERDEVICEREMAPPED = SDL_CONTROLLERDEVICEREMOVED + 1;
    public static final int SDL_FINGERDOWN  =  0x700;
    public static final int SDL_FINGERUP = SDL_FINGERDOWN  + 1;
    public static final int SDL_FINGERMOTION = SDL_FINGERUP + 1;
    public static final int SDL_DOLLARGESTURE  =  0x800;
    public static final int SDL_DOLLARRECORD = SDL_DOLLARGESTURE  + 1;
    public static final int SDL_MULTIGESTURE = SDL_DOLLARRECORD + 1;
    public static final int SDL_CLIPBOARDUPDATE  =  0x900;
    public static final int SDL_DROPFILE  =  0x1000;
    public static final int SDL_RENDER_TARGETS_RESET  =  0x2000;
    public static final int SDL_USEREVENT  =  0x8000;
    public static final int SDL_LASTEVENT  =  0xFFFF;
    //     SDL_eventaction
    public static final int SDL_ADDEVENT = 0;
    public static final int SDL_PEEKEVENT = SDL_ADDEVENT + 1;
    public static final int SDL_GETEVENT = SDL_PEEKEVENT + 1;
    // SDL_events.h
    public static final int SDL_RELEASED = 0 ;
    public static final int SDL_PRESSED = 1 ;
    public static final int SDL_IGNORE = 0 ;
    public static final int SDL_DISABLE = 0 ;
    public static final int SDL_ENABLE = 1 ;
    // SDL_haptic.h
    public static final int SDL_HAPTIC_POLAR = 0 ;
    public static final int SDL_HAPTIC_CARTESIAN = 1 ;
    public static final int SDL_HAPTIC_SPHERICAL = 2 ;
    public static final int SDL_HAPTIC_INFINITY = -1 ;
    // SDL_gamecontroller.h
    //     SDL_GameControllerBindType
    public static final int SDL_CONTROLLER_BINDTYPE_NONE  =  0;
    public static final int SDL_CONTROLLER_BINDTYPE_BUTTON = SDL_CONTROLLER_BINDTYPE_NONE  + 1;
    public static final int SDL_CONTROLLER_BINDTYPE_AXIS = SDL_CONTROLLER_BINDTYPE_BUTTON + 1;
    public static final int SDL_CONTROLLER_BINDTYPE_HAT = SDL_CONTROLLER_BINDTYPE_AXIS + 1;
    //     SDL_GameControllerAxis
    public static final int SDL_CONTROLLER_AXIS_INVALID  =  -1;
    public static final int SDL_CONTROLLER_AXIS_LEFTX = SDL_CONTROLLER_AXIS_INVALID  + 1;
    public static final int SDL_CONTROLLER_AXIS_LEFTY = SDL_CONTROLLER_AXIS_LEFTX + 1;
    public static final int SDL_CONTROLLER_AXIS_RIGHTX = SDL_CONTROLLER_AXIS_LEFTY + 1;
    public static final int SDL_CONTROLLER_AXIS_RIGHTY = SDL_CONTROLLER_AXIS_RIGHTX + 1;
    public static final int SDL_CONTROLLER_AXIS_TRIGGERLEFT = SDL_CONTROLLER_AXIS_RIGHTY + 1;
    public static final int SDL_CONTROLLER_AXIS_TRIGGERRIGHT = SDL_CONTROLLER_AXIS_TRIGGERLEFT + 1;
    public static final int SDL_CONTROLLER_AXIS_MAX = SDL_CONTROLLER_AXIS_TRIGGERRIGHT + 1;
    //     SDL_GameControllerButton
    public static final int SDL_CONTROLLER_BUTTON_INVALID  =  -1;
    public static final int SDL_CONTROLLER_BUTTON_A = SDL_CONTROLLER_BUTTON_INVALID  + 1;
    public static final int SDL_CONTROLLER_BUTTON_B = SDL_CONTROLLER_BUTTON_A + 1;
    public static final int SDL_CONTROLLER_BUTTON_X = SDL_CONTROLLER_BUTTON_B + 1;
    public static final int SDL_CONTROLLER_BUTTON_Y = SDL_CONTROLLER_BUTTON_X + 1;
    public static final int SDL_CONTROLLER_BUTTON_BACK = SDL_CONTROLLER_BUTTON_Y + 1;
    public static final int SDL_CONTROLLER_BUTTON_GUIDE = SDL_CONTROLLER_BUTTON_BACK + 1;
    public static final int SDL_CONTROLLER_BUTTON_START = SDL_CONTROLLER_BUTTON_GUIDE + 1;
    public static final int SDL_CONTROLLER_BUTTON_LEFTSTICK = SDL_CONTROLLER_BUTTON_START + 1;
    public static final int SDL_CONTROLLER_BUTTON_RIGHTSTICK = SDL_CONTROLLER_BUTTON_LEFTSTICK + 1;
    public static final int SDL_CONTROLLER_BUTTON_LEFTSHOULDER = SDL_CONTROLLER_BUTTON_RIGHTSTICK + 1;
    public static final int SDL_CONTROLLER_BUTTON_RIGHTSHOULDER = SDL_CONTROLLER_BUTTON_LEFTSHOULDER + 1;
    public static final int SDL_CONTROLLER_BUTTON_DPAD_UP = SDL_CONTROLLER_BUTTON_RIGHTSHOULDER + 1;
    public static final int SDL_CONTROLLER_BUTTON_DPAD_DOWN = SDL_CONTROLLER_BUTTON_DPAD_UP + 1;
    public static final int SDL_CONTROLLER_BUTTON_DPAD_LEFT = SDL_CONTROLLER_BUTTON_DPAD_DOWN + 1;
    public static final int SDL_CONTROLLER_BUTTON_DPAD_RIGHT = SDL_CONTROLLER_BUTTON_DPAD_LEFT + 1;
    public static final int SDL_CONTROLLER_BUTTON_MAX = SDL_CONTROLLER_BUTTON_DPAD_RIGHT + 1;
    // SDL_log.h
    //     SDL_LogPriority
    public static final int SDL_LOG_PRIORITY_VERBOSE  =  1;
    public static final int SDL_LOG_PRIORITY_DEBUG = SDL_LOG_PRIORITY_VERBOSE  + 1;
    public static final int SDL_LOG_PRIORITY_INFO = SDL_LOG_PRIORITY_DEBUG + 1;
    public static final int SDL_LOG_PRIORITY_WARN = SDL_LOG_PRIORITY_INFO + 1;
    public static final int SDL_LOG_PRIORITY_ERROR = SDL_LOG_PRIORITY_WARN + 1;
    public static final int SDL_LOG_PRIORITY_CRITICAL = SDL_LOG_PRIORITY_ERROR + 1;
    public static final int SDL_NUM_LOG_PRIORITIES = SDL_LOG_PRIORITY_CRITICAL + 1;
    // SDL_log.h
    public static final int SDL_MAX_LOG_MESSAGE = 4096 ;
    // SDL_keycode.h
    //     SDL_Keymod
    public static final int KMOD_NONE  =  0x0000;
    public static final int KMOD_LSHIFT  =  0x0001;
    public static final int KMOD_RSHIFT  =  0x0002;
    public static final int KMOD_LCTRL  =  0x0040;
    public static final int KMOD_RCTRL  =  0x0080;
    public static final int KMOD_LALT  =  0x0100;
    public static final int KMOD_RALT  =  0x0200;
    public static final int KMOD_LGUI  =  0x0400;
    public static final int KMOD_RGUI  =  0x0800;
    public static final int KMOD_NUM  =  0x1000;
    public static final int KMOD_CAPS  =  0x2000;
    public static final int KMOD_MODE  =  0x4000;
    public static final int KMOD_RESERVED  =  0x8000;
    // SDL_mouse.h
    //     SDL_SystemCursor
    public static final int SDL_SYSTEM_CURSOR_ARROW = 0;
    public static final int SDL_SYSTEM_CURSOR_IBEAM = SDL_SYSTEM_CURSOR_ARROW + 1;
    public static final int SDL_SYSTEM_CURSOR_WAIT = SDL_SYSTEM_CURSOR_IBEAM + 1;
    public static final int SDL_SYSTEM_CURSOR_CROSSHAIR = SDL_SYSTEM_CURSOR_WAIT + 1;
    public static final int SDL_SYSTEM_CURSOR_WAITARROW = SDL_SYSTEM_CURSOR_CROSSHAIR + 1;
    public static final int SDL_SYSTEM_CURSOR_SIZENWSE = SDL_SYSTEM_CURSOR_WAITARROW + 1;
    public static final int SDL_SYSTEM_CURSOR_SIZENESW = SDL_SYSTEM_CURSOR_SIZENWSE + 1;
    public static final int SDL_SYSTEM_CURSOR_SIZEWE = SDL_SYSTEM_CURSOR_SIZENESW + 1;
    public static final int SDL_SYSTEM_CURSOR_SIZENS = SDL_SYSTEM_CURSOR_SIZEWE + 1;
    public static final int SDL_SYSTEM_CURSOR_SIZEALL = SDL_SYSTEM_CURSOR_SIZENS + 1;
    public static final int SDL_SYSTEM_CURSOR_NO = SDL_SYSTEM_CURSOR_SIZEALL + 1;
    public static final int SDL_SYSTEM_CURSOR_HAND = SDL_SYSTEM_CURSOR_NO + 1;
    public static final int SDL_NUM_SYSTEM_CURSORS = SDL_SYSTEM_CURSOR_HAND + 1;
    // SDL_mouse.h
    public static final int SDL_BUTTON_LEFT = 1 ;
    public static final int SDL_BUTTON_MIDDLE = 2 ;
    public static final int SDL_BUTTON_RIGHT = 3 ;
    public static final int SDL_BUTTON_X1 = 4 ;
    public static final int SDL_BUTTON_X2 = 5 ;
    // SDL.h
    public static final int SDL_INIT_TIMER = 0x00000001 ;
    public static final int SDL_INIT_AUDIO = 0x00000010 ;
    public static final int SDL_INIT_VIDEO = 0x00000020 ;
    public static final int SDL_INIT_JOYSTICK = 0x00000200 ;
    public static final int SDL_INIT_HAPTIC = 0x00001000 ;
    public static final int SDL_INIT_GAMECONTROLLER = 0x00002000 ;
    public static final int SDL_INIT_EVENTS = 0x00004000 ;
    public static final int SDL_INIT_NOPARACHUTE = 0x00100000 ;
    // SDL_blendmode.h
    //     SDL_BlendMode
    public static final int SDL_BLENDMODE_NONE  =  0x00000000;
    public static final int SDL_BLENDMODE_BLEND  =  0x00000001;
    public static final int SDL_BLENDMODE_ADD  =  0x00000002;
    public static final int SDL_BLENDMODE_MOD  =  0x00000004;
    // SDL_error.h
    //     SDL_errorcode
    public static final int SDL_ENOMEM = 0;
    public static final int SDL_EFREAD = SDL_ENOMEM + 1;
    public static final int SDL_EFWRITE = SDL_EFREAD + 1;
    public static final int SDL_EFSEEK = SDL_EFWRITE + 1;
    public static final int SDL_UNSUPPORTED = SDL_EFSEEK + 1;
    public static final int SDL_LASTERROR = SDL_UNSUPPORTED + 1;
    // SDL_messagebox.h
    //     SDL_MessageBoxFlags
    public static final int SDL_MESSAGEBOX_ERROR  =  0x00000010;
    public static final int SDL_MESSAGEBOX_WARNING  =  0x00000020;
    public static final int SDL_MESSAGEBOX_INFORMATION  =  0x00000040;
    //     SDL_MessageBoxButtonFlags
    public static final int SDL_MESSAGEBOX_BUTTON_RETURNKEY_DEFAULT  =  0x00000001;
    public static final int SDL_MESSAGEBOX_BUTTON_ESCAPEKEY_DEFAULT  =  0x00000002;
    //     SDL_MessageBoxColorType
    public static final int SDL_MESSAGEBOX_COLOR_BACKGROUND = 0;
    public static final int SDL_MESSAGEBOX_COLOR_TEXT = SDL_MESSAGEBOX_COLOR_BACKGROUND + 1;
    public static final int SDL_MESSAGEBOX_COLOR_BUTTON_BORDER = SDL_MESSAGEBOX_COLOR_TEXT + 1;
    public static final int SDL_MESSAGEBOX_COLOR_BUTTON_BACKGROUND = SDL_MESSAGEBOX_COLOR_BUTTON_BORDER + 1;
    public static final int SDL_MESSAGEBOX_COLOR_BUTTON_SELECTED = SDL_MESSAGEBOX_COLOR_BUTTON_BACKGROUND + 1;
    public static final int SDL_MESSAGEBOX_COLOR_MAX = SDL_MESSAGEBOX_COLOR_BUTTON_SELECTED + 1;
    // SDL_cpuinfo.h
    public static final int SDL_CACHELINE_SIZE = 128 ;
    // SDL_pixels.h
    public static final int SDL_ALPHA_OPAQUE = 255 ;
    public static final int SDL_ALPHA_TRANSPARENT = 0 ;
    // SDL_video.h
    //     SDL_WindowFlags
    public static final int SDL_WINDOW_FULLSCREEN  =  0x00000001;
    public static final int SDL_WINDOW_OPENGL  =  0x00000002;
    public static final int SDL_WINDOW_SHOWN  =  0x00000004;
    public static final int SDL_WINDOW_HIDDEN  =  0x00000008;
    public static final int SDL_WINDOW_BORDERLESS  =  0x00000010;
    public static final int SDL_WINDOW_RESIZABLE  =  0x00000020;
    public static final int SDL_WINDOW_MINIMIZED  =  0x00000040;
    public static final int SDL_WINDOW_MAXIMIZED  =  0x00000080;
    public static final int SDL_WINDOW_INPUT_GRABBED  =  0x00000100;
    public static final int SDL_WINDOW_INPUT_FOCUS  =  0x00000200;
    public static final int SDL_WINDOW_MOUSE_FOCUS  =  0x00000400;
    public static final int SDL_WINDOW_FULLSCREEN_DESKTOP  =  ( SDL_WINDOW_FULLSCREEN | 0x00001000 );
    public static final int SDL_WINDOW_FOREIGN  =  0x00000800;
    public static final int SDL_WINDOW_ALLOW_HIGHDPI  =  0x00002000;
    //     SDL_WindowEventID
    public static final int SDL_WINDOWEVENT_NONE = 0;
    public static final int SDL_WINDOWEVENT_SHOWN = SDL_WINDOWEVENT_NONE + 1;
    public static final int SDL_WINDOWEVENT_HIDDEN = SDL_WINDOWEVENT_SHOWN + 1;
    public static final int SDL_WINDOWEVENT_EXPOSED = SDL_WINDOWEVENT_HIDDEN + 1;
    public static final int SDL_WINDOWEVENT_MOVED = SDL_WINDOWEVENT_EXPOSED + 1;
    public static final int SDL_WINDOWEVENT_RESIZED = SDL_WINDOWEVENT_MOVED + 1;
    public static final int SDL_WINDOWEVENT_SIZE_CHANGED = SDL_WINDOWEVENT_RESIZED + 1;
    public static final int SDL_WINDOWEVENT_MINIMIZED = SDL_WINDOWEVENT_SIZE_CHANGED + 1;
    public static final int SDL_WINDOWEVENT_MAXIMIZED = SDL_WINDOWEVENT_MINIMIZED + 1;
    public static final int SDL_WINDOWEVENT_RESTORED = SDL_WINDOWEVENT_MAXIMIZED + 1;
    public static final int SDL_WINDOWEVENT_ENTER = SDL_WINDOWEVENT_RESTORED + 1;
    public static final int SDL_WINDOWEVENT_LEAVE = SDL_WINDOWEVENT_ENTER + 1;
    public static final int SDL_WINDOWEVENT_FOCUS_GAINED = SDL_WINDOWEVENT_LEAVE + 1;
    public static final int SDL_WINDOWEVENT_FOCUS_LOST = SDL_WINDOWEVENT_FOCUS_GAINED + 1;
    public static final int SDL_WINDOWEVENT_CLOSE = SDL_WINDOWEVENT_FOCUS_LOST + 1;
    //     SDL_GLattr
    public static final int SDL_GL_RED_SIZE = 0;
    public static final int SDL_GL_GREEN_SIZE = SDL_GL_RED_SIZE + 1;
    public static final int SDL_GL_BLUE_SIZE = SDL_GL_GREEN_SIZE + 1;
    public static final int SDL_GL_ALPHA_SIZE = SDL_GL_BLUE_SIZE + 1;
    public static final int SDL_GL_BUFFER_SIZE = SDL_GL_ALPHA_SIZE + 1;
    public static final int SDL_GL_DOUBLEBUFFER = SDL_GL_BUFFER_SIZE + 1;
    public static final int SDL_GL_DEPTH_SIZE = SDL_GL_DOUBLEBUFFER + 1;
    public static final int SDL_GL_STENCIL_SIZE = SDL_GL_DEPTH_SIZE + 1;
    public static final int SDL_GL_ACCUM_RED_SIZE = SDL_GL_STENCIL_SIZE + 1;
    public static final int SDL_GL_ACCUM_GREEN_SIZE = SDL_GL_ACCUM_RED_SIZE + 1;
    public static final int SDL_GL_ACCUM_BLUE_SIZE = SDL_GL_ACCUM_GREEN_SIZE + 1;
    public static final int SDL_GL_ACCUM_ALPHA_SIZE = SDL_GL_ACCUM_BLUE_SIZE + 1;
    public static final int SDL_GL_STEREO = SDL_GL_ACCUM_ALPHA_SIZE + 1;
    public static final int SDL_GL_MULTISAMPLEBUFFERS = SDL_GL_STEREO + 1;
    public static final int SDL_GL_MULTISAMPLESAMPLES = SDL_GL_MULTISAMPLEBUFFERS + 1;
    public static final int SDL_GL_ACCELERATED_VISUAL = SDL_GL_MULTISAMPLESAMPLES + 1;
    public static final int SDL_GL_RETAINED_BACKING = SDL_GL_ACCELERATED_VISUAL + 1;
    public static final int SDL_GL_CONTEXT_MAJOR_VERSION = SDL_GL_RETAINED_BACKING + 1;
    public static final int SDL_GL_CONTEXT_MINOR_VERSION = SDL_GL_CONTEXT_MAJOR_VERSION + 1;
    public static final int SDL_GL_CONTEXT_EGL = SDL_GL_CONTEXT_MINOR_VERSION + 1;
    public static final int SDL_GL_CONTEXT_FLAGS = SDL_GL_CONTEXT_EGL + 1;
    public static final int SDL_GL_CONTEXT_PROFILE_MASK = SDL_GL_CONTEXT_FLAGS + 1;
    public static final int SDL_GL_SHARE_WITH_CURRENT_CONTEXT = SDL_GL_CONTEXT_PROFILE_MASK + 1;
    public static final int SDL_GL_FRAMEBUFFER_SRGB_CAPABLE = SDL_GL_SHARE_WITH_CURRENT_CONTEXT + 1;
    //     SDL_GLprofile
    public static final int SDL_GL_CONTEXT_PROFILE_CORE  =  0x0001;
    public static final int SDL_GL_CONTEXT_PROFILE_COMPATIBILITY  =  0x0002;
    public static final int SDL_GL_CONTEXT_PROFILE_ES  =  0x0004;
    //     SDL_GLcontextFlag
    public static final int SDL_GL_CONTEXT_DEBUG_FLAG  =  0x0001;
    public static final int SDL_GL_CONTEXT_FORWARD_COMPATIBLE_FLAG  =  0x0002;
    public static final int SDL_GL_CONTEXT_ROBUST_ACCESS_FLAG  =  0x0004;
    public static final int SDL_GL_CONTEXT_RESET_ISOLATION_FLAG  =  0x0008;
    // SDL_video.h
    public static final int SDL_WINDOWPOS_UNDEFINED_MASK = 0x1 ;
    public static final int SDL_WINDOWPOS_CENTERED_MASK = 0x2 ;
    // SDL_thread.h
    //     SDL_ThreadPriority
    public static final int SDL_THREAD_PRIORITY_LOW = 0;
    public static final int SDL_THREAD_PRIORITY_NORMAL = SDL_THREAD_PRIORITY_LOW + 1;
    public static final int SDL_THREAD_PRIORITY_HIGH = SDL_THREAD_PRIORITY_NORMAL + 1;
    // SDL_rwops.h
    public static final int SDL_RWOPS_UNKNOWN = 0 ;
    public static final int SDL_RWOPS_WINFILE = 1 ;
    public static final int SDL_RWOPS_STDFILE = 2 ;
    public static final int SDL_RWOPS_JNIFILE = 3 ;
    public static final int SDL_RWOPS_MEMORY = 4 ;
    public static final int SDL_RWOPS_MEMORY_RO = 5 ;
    // SDL_render.h
    //     SDL_RendererFlags
    public static final int SDL_RENDERER_SOFTWARE  =  0x00000001;
    public static final int SDL_RENDERER_ACCELERATED  =  0x00000002;
    public static final int SDL_RENDERER_PRESENTVSYNC  =  0x00000004;
    public static final int SDL_RENDERER_TARGETTEXTURE  =  0x00000008;
    //     SDL_TextureAccess
    public static final int SDL_TEXTUREACCESS_STATIC = 0;
    public static final int SDL_TEXTUREACCESS_STREAMING = SDL_TEXTUREACCESS_STATIC + 1;
    public static final int SDL_TEXTUREACCESS_TARGET = SDL_TEXTUREACCESS_STREAMING + 1;
    //     SDL_TextureModulate
    public static final int SDL_TEXTUREMODULATE_NONE  =  0x00000000;
    public static final int SDL_TEXTUREMODULATE_COLOR  =  0x00000001;
    public static final int SDL_TEXTUREMODULATE_ALPHA  =  0x00000002;
    //     SDL_RendererFlip
    public static final int SDL_FLIP_NONE  =  0x00000000;
    public static final int SDL_FLIP_HORIZONTAL  =  0x00000001;
    public static final int SDL_FLIP_VERTICAL  =  0x00000002;
    // SDL_scancode.h
    //     SDL_Scancode
    public static final int SDL_SCANCODE_UNKNOWN  =  0;
    public static final int SDL_SCANCODE_A  =  4;
    public static final int SDL_SCANCODE_B  =  5;
    public static final int SDL_SCANCODE_C  =  6;
    public static final int SDL_SCANCODE_D  =  7;
    public static final int SDL_SCANCODE_E  =  8;
    public static final int SDL_SCANCODE_F  =  9;
    public static final int SDL_SCANCODE_G  =  10;
    public static final int SDL_SCANCODE_H  =  11;
    public static final int SDL_SCANCODE_I  =  12;
    public static final int SDL_SCANCODE_J  =  13;
    public static final int SDL_SCANCODE_K  =  14;
    public static final int SDL_SCANCODE_L  =  15;
    public static final int SDL_SCANCODE_M  =  16;
    public static final int SDL_SCANCODE_N  =  17;
    public static final int SDL_SCANCODE_O  =  18;
    public static final int SDL_SCANCODE_P  =  19;
    public static final int SDL_SCANCODE_Q  =  20;
    public static final int SDL_SCANCODE_R  =  21;
    public static final int SDL_SCANCODE_S  =  22;
    public static final int SDL_SCANCODE_T  =  23;
    public static final int SDL_SCANCODE_U  =  24;
    public static final int SDL_SCANCODE_V  =  25;
    public static final int SDL_SCANCODE_W  =  26;
    public static final int SDL_SCANCODE_X  =  27;
    public static final int SDL_SCANCODE_Y  =  28;
    public static final int SDL_SCANCODE_Z  =  29;
    public static final int SDL_SCANCODE_1  =  30;
    public static final int SDL_SCANCODE_2  =  31;
    public static final int SDL_SCANCODE_3  =  32;
    public static final int SDL_SCANCODE_4  =  33;
    public static final int SDL_SCANCODE_5  =  34;
    public static final int SDL_SCANCODE_6  =  35;
    public static final int SDL_SCANCODE_7  =  36;
    public static final int SDL_SCANCODE_8  =  37;
    public static final int SDL_SCANCODE_9  =  38;
    public static final int SDL_SCANCODE_0  =  39;
    public static final int SDL_SCANCODE_RETURN  =  40;
    public static final int SDL_SCANCODE_ESCAPE  =  41;
    public static final int SDL_SCANCODE_BACKSPACE  =  42;
    public static final int SDL_SCANCODE_TAB  =  43;
    public static final int SDL_SCANCODE_SPACE  =  44;
    public static final int SDL_SCANCODE_MINUS  =  45;
    public static final int SDL_SCANCODE_EQUALS  =  46;
    public static final int SDL_SCANCODE_LEFTBRACKET  =  47;
    public static final int SDL_SCANCODE_RIGHTBRACKET  =  48;
    public static final int SDL_SCANCODE_BACKSLASH  =  49;
    public static final int SDL_SCANCODE_NONUSHASH  =  50;
    public static final int SDL_SCANCODE_SEMICOLON  =  51;
    public static final int SDL_SCANCODE_APOSTROPHE  =  52;
    public static final int SDL_SCANCODE_GRAVE  =  53;
    public static final int SDL_SCANCODE_COMMA  =  54;
    public static final int SDL_SCANCODE_PERIOD  =  55;
    public static final int SDL_SCANCODE_SLASH  =  56;
    public static final int SDL_SCANCODE_CAPSLOCK  =  57;
    public static final int SDL_SCANCODE_F1  =  58;
    public static final int SDL_SCANCODE_F2  =  59;
    public static final int SDL_SCANCODE_F3  =  60;
    public static final int SDL_SCANCODE_F4  =  61;
    public static final int SDL_SCANCODE_F5  =  62;
    public static final int SDL_SCANCODE_F6  =  63;
    public static final int SDL_SCANCODE_F7  =  64;
    public static final int SDL_SCANCODE_F8  =  65;
    public static final int SDL_SCANCODE_F9  =  66;
    public static final int SDL_SCANCODE_F10  =  67;
    public static final int SDL_SCANCODE_F11  =  68;
    public static final int SDL_SCANCODE_F12  =  69;
    public static final int SDL_SCANCODE_PRINTSCREEN  =  70;
    public static final int SDL_SCANCODE_SCROLLLOCK  =  71;
    public static final int SDL_SCANCODE_PAUSE  =  72;
    public static final int SDL_SCANCODE_INSERT  =  73;
    public static final int SDL_SCANCODE_HOME  =  74;
    public static final int SDL_SCANCODE_PAGEUP  =  75;
    public static final int SDL_SCANCODE_DELETE  =  76;
    public static final int SDL_SCANCODE_END  =  77;
    public static final int SDL_SCANCODE_PAGEDOWN  =  78;
    public static final int SDL_SCANCODE_RIGHT  =  79;
    public static final int SDL_SCANCODE_LEFT  =  80;
    public static final int SDL_SCANCODE_DOWN  =  81;
    public static final int SDL_SCANCODE_UP  =  82;
    public static final int SDL_SCANCODE_NUMLOCKCLEAR  =  83;
    public static final int SDL_SCANCODE_KP_DIVIDE  =  84;
    public static final int SDL_SCANCODE_KP_MULTIPLY  =  85;
    public static final int SDL_SCANCODE_KP_MINUS  =  86;
    public static final int SDL_SCANCODE_KP_PLUS  =  87;
    public static final int SDL_SCANCODE_KP_ENTER  =  88;
    public static final int SDL_SCANCODE_KP_1  =  89;
    public static final int SDL_SCANCODE_KP_2  =  90;
    public static final int SDL_SCANCODE_KP_3  =  91;
    public static final int SDL_SCANCODE_KP_4  =  92;
    public static final int SDL_SCANCODE_KP_5  =  93;
    public static final int SDL_SCANCODE_KP_6  =  94;
    public static final int SDL_SCANCODE_KP_7  =  95;
    public static final int SDL_SCANCODE_KP_8  =  96;
    public static final int SDL_SCANCODE_KP_9  =  97;
    public static final int SDL_SCANCODE_KP_0  =  98;
    public static final int SDL_SCANCODE_KP_PERIOD  =  99;
    public static final int SDL_SCANCODE_NONUSBACKSLASH  =  100;
    public static final int SDL_SCANCODE_APPLICATION  =  101;
    public static final int SDL_SCANCODE_POWER  =  102;
    public static final int SDL_SCANCODE_KP_EQUALS  =  103;
    public static final int SDL_SCANCODE_F13  =  104;
    public static final int SDL_SCANCODE_F14  =  105;
    public static final int SDL_SCANCODE_F15  =  106;
    public static final int SDL_SCANCODE_F16  =  107;
    public static final int SDL_SCANCODE_F17  =  108;
    public static final int SDL_SCANCODE_F18  =  109;
    public static final int SDL_SCANCODE_F19  =  110;
    public static final int SDL_SCANCODE_F20  =  111;
    public static final int SDL_SCANCODE_F21  =  112;
    public static final int SDL_SCANCODE_F22  =  113;
    public static final int SDL_SCANCODE_F23  =  114;
    public static final int SDL_SCANCODE_F24  =  115;
    public static final int SDL_SCANCODE_EXECUTE  =  116;
    public static final int SDL_SCANCODE_HELP  =  117;
    public static final int SDL_SCANCODE_MENU  =  118;
    public static final int SDL_SCANCODE_SELECT  =  119;
    public static final int SDL_SCANCODE_STOP  =  120;
    public static final int SDL_SCANCODE_AGAIN  =  121;
    public static final int SDL_SCANCODE_UNDO  =  122;
    public static final int SDL_SCANCODE_CUT  =  123;
    public static final int SDL_SCANCODE_COPY  =  124;
    public static final int SDL_SCANCODE_PASTE  =  125;
    public static final int SDL_SCANCODE_FIND  =  126;
    public static final int SDL_SCANCODE_MUTE  =  127;
    public static final int SDL_SCANCODE_VOLUMEUP  =  128;
    public static final int SDL_SCANCODE_VOLUMEDOWN  =  129;
    public static final int SDL_SCANCODE_KP_COMMA  =  133;
    public static final int SDL_SCANCODE_KP_EQUALSAS400  =  134;
    public static final int SDL_SCANCODE_INTERNATIONAL1  =  135;
    public static final int SDL_SCANCODE_INTERNATIONAL2  =  136;
    public static final int SDL_SCANCODE_INTERNATIONAL3  =  137;
    public static final int SDL_SCANCODE_INTERNATIONAL4  =  138;
    public static final int SDL_SCANCODE_INTERNATIONAL5  =  139;
    public static final int SDL_SCANCODE_INTERNATIONAL6  =  140;
    public static final int SDL_SCANCODE_INTERNATIONAL7  =  141;
    public static final int SDL_SCANCODE_INTERNATIONAL8  =  142;
    public static final int SDL_SCANCODE_INTERNATIONAL9  =  143;
    public static final int SDL_SCANCODE_LANG1  =  144;
    public static final int SDL_SCANCODE_LANG2  =  145;
    public static final int SDL_SCANCODE_LANG3  =  146;
    public static final int SDL_SCANCODE_LANG4  =  147;
    public static final int SDL_SCANCODE_LANG5  =  148;
    public static final int SDL_SCANCODE_LANG6  =  149;
    public static final int SDL_SCANCODE_LANG7  =  150;
    public static final int SDL_SCANCODE_LANG8  =  151;
    public static final int SDL_SCANCODE_LANG9  =  152;
    public static final int SDL_SCANCODE_ALTERASE  =  153;
    public static final int SDL_SCANCODE_SYSREQ  =  154;
    public static final int SDL_SCANCODE_CANCEL  =  155;
    public static final int SDL_SCANCODE_CLEAR  =  156;
    public static final int SDL_SCANCODE_PRIOR  =  157;
    public static final int SDL_SCANCODE_RETURN2  =  158;
    public static final int SDL_SCANCODE_SEPARATOR  =  159;
    public static final int SDL_SCANCODE_OUT  =  160;
    public static final int SDL_SCANCODE_OPER  =  161;
    public static final int SDL_SCANCODE_CLEARAGAIN  =  162;
    public static final int SDL_SCANCODE_CRSEL  =  163;
    public static final int SDL_SCANCODE_EXSEL  =  164;
    public static final int SDL_SCANCODE_KP_00  =  176;
    public static final int SDL_SCANCODE_KP_000  =  177;
    public static final int SDL_SCANCODE_THOUSANDSSEPARATOR  =  178;
    public static final int SDL_SCANCODE_DECIMALSEPARATOR  =  179;
    public static final int SDL_SCANCODE_CURRENCYUNIT  =  180;
    public static final int SDL_SCANCODE_CURRENCYSUBUNIT  =  181;
    public static final int SDL_SCANCODE_KP_LEFTPAREN  =  182;
    public static final int SDL_SCANCODE_KP_RIGHTPAREN  =  183;
    public static final int SDL_SCANCODE_KP_LEFTBRACE  =  184;
    public static final int SDL_SCANCODE_KP_RIGHTBRACE  =  185;
    public static final int SDL_SCANCODE_KP_TAB  =  186;
    public static final int SDL_SCANCODE_KP_BACKSPACE  =  187;
    public static final int SDL_SCANCODE_KP_A  =  188;
    public static final int SDL_SCANCODE_KP_B  =  189;
    public static final int SDL_SCANCODE_KP_C  =  190;
    public static final int SDL_SCANCODE_KP_D  =  191;
    public static final int SDL_SCANCODE_KP_E  =  192;
    public static final int SDL_SCANCODE_KP_F  =  193;
    public static final int SDL_SCANCODE_KP_XOR  =  194;
    public static final int SDL_SCANCODE_KP_POWER  =  195;
    public static final int SDL_SCANCODE_KP_PERCENT  =  196;
    public static final int SDL_SCANCODE_KP_LESS  =  197;
    public static final int SDL_SCANCODE_KP_GREATER  =  198;
    public static final int SDL_SCANCODE_KP_AMPERSAND  =  199;
    public static final int SDL_SCANCODE_KP_DBLAMPERSAND  =  200;
    public static final int SDL_SCANCODE_KP_VERTICALBAR  =  201;
    public static final int SDL_SCANCODE_KP_DBLVERTICALBAR  =  202;
    public static final int SDL_SCANCODE_KP_COLON  =  203;
    public static final int SDL_SCANCODE_KP_HASH  =  204;
    public static final int SDL_SCANCODE_KP_SPACE  =  205;
    public static final int SDL_SCANCODE_KP_AT  =  206;
    public static final int SDL_SCANCODE_KP_EXCLAM  =  207;
    public static final int SDL_SCANCODE_KP_MEMSTORE  =  208;
    public static final int SDL_SCANCODE_KP_MEMRECALL  =  209;
    public static final int SDL_SCANCODE_KP_MEMCLEAR  =  210;
    public static final int SDL_SCANCODE_KP_MEMADD  =  211;
    public static final int SDL_SCANCODE_KP_MEMSUBTRACT  =  212;
    public static final int SDL_SCANCODE_KP_MEMMULTIPLY  =  213;
    public static final int SDL_SCANCODE_KP_MEMDIVIDE  =  214;
    public static final int SDL_SCANCODE_KP_PLUSMINUS  =  215;
    public static final int SDL_SCANCODE_KP_CLEAR  =  216;
    public static final int SDL_SCANCODE_KP_CLEARENTRY  =  217;
    public static final int SDL_SCANCODE_KP_BINARY  =  218;
    public static final int SDL_SCANCODE_KP_OCTAL  =  219;
    public static final int SDL_SCANCODE_KP_DECIMAL  =  220;
    public static final int SDL_SCANCODE_KP_HEXADECIMAL  =  221;
    public static final int SDL_SCANCODE_LCTRL  =  224;
    public static final int SDL_SCANCODE_LSHIFT  =  225;
    public static final int SDL_SCANCODE_LALT  =  226;
    public static final int SDL_SCANCODE_LGUI  =  227;
    public static final int SDL_SCANCODE_RCTRL  =  228;
    public static final int SDL_SCANCODE_RSHIFT  =  229;
    public static final int SDL_SCANCODE_RALT  =  230;
    public static final int SDL_SCANCODE_RGUI  =  231;
    public static final int SDL_SCANCODE_MODE  =  257;
    public static final int SDL_SCANCODE_AUDIONEXT  =  258;
    public static final int SDL_SCANCODE_AUDIOPREV  =  259;
    public static final int SDL_SCANCODE_AUDIOSTOP  =  260;
    public static final int SDL_SCANCODE_AUDIOPLAY  =  261;
    public static final int SDL_SCANCODE_AUDIOMUTE  =  262;
    public static final int SDL_SCANCODE_MEDIASELECT  =  263;
    public static final int SDL_SCANCODE_WWW  =  264;
    public static final int SDL_SCANCODE_MAIL  =  265;
    public static final int SDL_SCANCODE_CALCULATOR  =  266;
    public static final int SDL_SCANCODE_COMPUTER  =  267;
    public static final int SDL_SCANCODE_AC_SEARCH  =  268;
    public static final int SDL_SCANCODE_AC_HOME  =  269;
    public static final int SDL_SCANCODE_AC_BACK  =  270;
    public static final int SDL_SCANCODE_AC_FORWARD  =  271;
    public static final int SDL_SCANCODE_AC_STOP  =  272;
    public static final int SDL_SCANCODE_AC_REFRESH  =  273;
    public static final int SDL_SCANCODE_AC_BOOKMARKS  =  274;
    public static final int SDL_SCANCODE_BRIGHTNESSDOWN  =  275;
    public static final int SDL_SCANCODE_BRIGHTNESSUP  =  276;
    public static final int SDL_SCANCODE_DISPLAYSWITCH  =  277;
    public static final int SDL_SCANCODE_KBDILLUMTOGGLE  =  278;
    public static final int SDL_SCANCODE_KBDILLUMDOWN  =  279;
    public static final int SDL_SCANCODE_KBDILLUMUP  =  280;
    public static final int SDL_SCANCODE_EJECT  =  281;
    public static final int SDL_SCANCODE_SLEEP  =  282;
    public static final int SDL_SCANCODE_APP1  =  283;
    public static final int SDL_SCANCODE_APP2  =  284;
    public static final int SDL_NUM_SCANCODES  =  512;
    // SDL_stdinc.h
    //     SDL_bool
    public static final int SDL_FALSE  =  0;
    public static final int SDL_TRUE  =  1;
    //     SDL_DUMMY_ENUM
    public static final int DUMMY_ENUM_VALUE = 0;
    // SDL_version.h
    public static final int SDL_MAJOR_VERSION = 2 ;
    public static final int SDL_MINOR_VERSION = 0 ;
    public static final int SDL_PATCHLEVEL = 3 ;
    // SDL_surface.h
    public static final int SDL_SWSURFACE = 0 ;
    public static final int SDL_PREALLOC = 0x00000001 ;
    public static final int SDL_RLEACCEL = 0x00000002 ;
    public static final int SDL_DONTFREE = 0x00000004 ;
    // SDL_power.h
    //     SDL_PowerState
    public static final int SDL_POWERSTATE_UNKNOWN = 0;
    public static final int SDL_POWERSTATE_ON_BATTERY = SDL_POWERSTATE_UNKNOWN + 1;
    public static final int SDL_POWERSTATE_NO_BATTERY = SDL_POWERSTATE_ON_BATTERY + 1;
    public static final int SDL_POWERSTATE_CHARGING = SDL_POWERSTATE_NO_BATTERY + 1;
    public static final int SDL_POWERSTATE_CHARGED = SDL_POWERSTATE_CHARGING + 1;
    // SDL_endian.h
    public static final int SDL_LIL_ENDIAN = 1234 ;
    public static final int SDL_BIG_ENDIAN = 4321 ;
    // SDL_revision.h
    public static final int SDL_REVISION_NUMBER = 8628 ;
    // SDL_joystick.h
    public static final int SDL_HAT_CENTERED = 0x00 ;
    public static final int SDL_HAT_UP = 0x01 ;
    public static final int SDL_HAT_RIGHT = 0x02 ;
    public static final int SDL_HAT_DOWN = 0x04 ;
    public static final int SDL_HAT_LEFT = 0x08 ;
    // SDL_mutex.h
    public static final int SDL_MUTEX_TIMEDOUT = 1 ;
    // SDL_config.h
    public static final int SDL_AUDIO_DRIVER_ALSA = 1 ;
    public static final int SDL_AUDIO_DRIVER_DISK = 1 ;
    public static final int SDL_AUDIO_DRIVER_DUMMY = 1 ;
    public static final int SDL_AUDIO_DRIVER_OSS = 1 ;
    public static final int SDL_INPUT_LINUXEV = 1 ;
    public static final int SDL_INPUT_LINUXKD = 1 ;
    public static final int SDL_JOYSTICK_LINUX = 1 ;
    public static final int SDL_HAPTIC_LINUX = 1 ;
    public static final int SDL_LOADSO_DLOPEN = 1 ;
    public static final int SDL_THREAD_PTHREAD = 1 ;
    public static final int SDL_THREAD_PTHREAD_RECURSIVE_MUTEX = 1 ;
    public static final int SDL_TIMER_UNIX = 1 ;
    public static final int SDL_VIDEO_DRIVER_DUMMY = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11 = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11_XCURSOR = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11_XINERAMA = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11_XINPUT2 = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11_XINPUT2_SUPPORTS_MULTITOUCH = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11_XRANDR = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11_XSCRNSAVER = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11_XSHAPE = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11_XVIDMODE = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11_SUPPORTS_GENERIC_EVENTS = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11_CONST_PARAM_XDATA32 = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11_CONST_PARAM_XEXTADDDISPLAY = 1 ;
    public static final int SDL_VIDEO_DRIVER_X11_HAS_XKBKEYCODETOKEYSYM = 1 ;
    public static final int SDL_VIDEO_RENDER_OGL = 1 ;
    public static final int SDL_VIDEO_RENDER_OGL_ES2 = 1 ;
    public static final int SDL_VIDEO_OPENGL = 1 ;
    public static final int SDL_VIDEO_OPENGL_ES2 = 1 ;
    public static final int SDL_VIDEO_OPENGL_EGL = 1 ;
    public static final int SDL_VIDEO_OPENGL_GLX = 1 ;
    public static final int SDL_POWER_LINUX = 1 ;
    public static final int SDL_FILESYSTEM_UNIX = 1 ;
    public static final int SDL_ASSEMBLY_ROUTINES = 1 ;
    // SDL_syswm.h
    //     SDL_SYSWM_TYPE
    public static final int SDL_SYSWM_UNKNOWN = 0;
    public static final int SDL_SYSWM_WINDOWS = SDL_SYSWM_UNKNOWN + 1;
    public static final int SDL_SYSWM_X11 = SDL_SYSWM_WINDOWS + 1;
    public static final int SDL_SYSWM_DIRECTFB = SDL_SYSWM_X11 + 1;
    public static final int SDL_SYSWM_COCOA = SDL_SYSWM_DIRECTFB + 1;
    public static final int SDL_SYSWM_UIKIT = SDL_SYSWM_COCOA + 1;
    public static final int SDL_SYSWM_WAYLAND = SDL_SYSWM_UIKIT + 1;
    public static final int SDL_SYSWM_MIR = SDL_SYSWM_WAYLAND + 1;
    public static final int SDL_SYSWM_WINRT = SDL_SYSWM_MIR + 1;
    // SDL_hints.h
    //     SDL_HintPriority
    public static final int SDL_HINT_DEFAULT = 0;
    public static final int SDL_HINT_NORMAL = SDL_HINT_DEFAULT + 1;
    public static final int SDL_HINT_OVERRIDE = SDL_HINT_NORMAL + 1;
    //structs
    public static class SDL_WindowShapeParams  extends Union {
        public byte binarizationCutoff ;
        public SDL_Color colorKey ;
    }
    public static class SDL_WindowShapeMode  extends Structure {
        public int mode ;
        public SDL_WindowShapeParams parameters ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "mode","parameters" });
        }
    }
    public static class SDL_WindowShapeMode_byvalue extends SDL_WindowShapeMode implements Structure.ByValue {}
    public static class SDL_WindowShapeMode_byref extends SDL_WindowShapeMode implements Structure.ByReference {}
    public static class SDL_AudioSpec extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_AudioSpec_byvalue extends SDL_AudioSpec implements Structure.ByValue {}
    public static class SDL_AudioSpec_byref extends SDL_AudioSpec implements Structure.ByReference {}
    public static class SDL_AudioCVT extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_AudioCVT_byvalue extends SDL_AudioCVT implements Structure.ByValue {}
    public static class SDL_AudioCVT_byref extends SDL_AudioCVT implements Structure.ByReference {}
    public static class SDL_CommonEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp" });
        }
    }
    public static class SDL_CommonEvent_byvalue extends SDL_CommonEvent implements Structure.ByValue {}
    public static class SDL_CommonEvent_byref extends SDL_CommonEvent implements Structure.ByReference {}
    public static class SDL_WindowEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int windowID ;
        public byte event ;
        public byte padding1 ;
        public byte padding2 ;
        public byte padding3 ;
        public int data1 ;
        public int data2 ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","windowID","event","padding1","padding2","padding3","data1","data2" });
        }
    }
    public static class SDL_WindowEvent_byvalue extends SDL_WindowEvent implements Structure.ByValue {}
    public static class SDL_WindowEvent_byref extends SDL_WindowEvent implements Structure.ByReference {}
    public static class SDL_KeyboardEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int windowID ;
        public byte state ;
        public byte repeat ;
        public byte padding2 ;
        public byte padding3 ;
        public SDL_Keysym keysym ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","windowID","state","repeat","padding2","padding3","keysym" });
        }
    }
    public static class SDL_KeyboardEvent_byvalue extends SDL_KeyboardEvent implements Structure.ByValue {}
    public static class SDL_KeyboardEvent_byref extends SDL_KeyboardEvent implements Structure.ByReference {}
    public static class SDL_TextEditingEvent extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_TextEditingEvent_byvalue extends SDL_TextEditingEvent implements Structure.ByValue {}
    public static class SDL_TextEditingEvent_byref extends SDL_TextEditingEvent implements Structure.ByReference {}
    public static class SDL_TextInputEvent extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_TextInputEvent_byvalue extends SDL_TextInputEvent implements Structure.ByValue {}
    public static class SDL_TextInputEvent_byref extends SDL_TextInputEvent implements Structure.ByReference {}
    public static class SDL_MouseMotionEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int windowID ;
        public int which ;
        public int state ;
        public int x ;
        public int y ;
        public int xrel ;
        public int yrel ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","windowID","which","state","x","y","xrel","yrel" });
        }
    }
    public static class SDL_MouseMotionEvent_byvalue extends SDL_MouseMotionEvent implements Structure.ByValue {}
    public static class SDL_MouseMotionEvent_byref extends SDL_MouseMotionEvent implements Structure.ByReference {}
    public static class SDL_MouseButtonEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int windowID ;
        public int which ;
        public byte button ;
        public byte state ;
        public byte clicks ;
        public byte padding1 ;
        public int x ;
        public int y ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","windowID","which","button","state","clicks","padding1","x","y" });
        }
    }
    public static class SDL_MouseButtonEvent_byvalue extends SDL_MouseButtonEvent implements Structure.ByValue {}
    public static class SDL_MouseButtonEvent_byref extends SDL_MouseButtonEvent implements Structure.ByReference {}
    public static class SDL_MouseWheelEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int windowID ;
        public int which ;
        public int x ;
        public int y ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","windowID","which","x","y" });
        }
    }
    public static class SDL_MouseWheelEvent_byvalue extends SDL_MouseWheelEvent implements Structure.ByValue {}
    public static class SDL_MouseWheelEvent_byref extends SDL_MouseWheelEvent implements Structure.ByReference {}
    public static class SDL_JoyAxisEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int which ;
        public byte axis ;
        public byte padding1 ;
        public byte padding2 ;
        public byte padding3 ;
        public short value ;
        public short padding4 ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","which","axis","padding1","padding2","padding3","value","padding4" });
        }
    }
    public static class SDL_JoyAxisEvent_byvalue extends SDL_JoyAxisEvent implements Structure.ByValue {}
    public static class SDL_JoyAxisEvent_byref extends SDL_JoyAxisEvent implements Structure.ByReference {}
    public static class SDL_JoyBallEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int which ;
        public byte ball ;
        public byte padding1 ;
        public byte padding2 ;
        public byte padding3 ;
        public short xrel ;
        public short yrel ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","which","ball","padding1","padding2","padding3","xrel","yrel" });
        }
    }
    public static class SDL_JoyBallEvent_byvalue extends SDL_JoyBallEvent implements Structure.ByValue {}
    public static class SDL_JoyBallEvent_byref extends SDL_JoyBallEvent implements Structure.ByReference {}
    public static class SDL_JoyHatEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int which ;
        public byte hat ;
        public byte value ;
        public byte padding1 ;
        public byte padding2 ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","which","hat","value","padding1","padding2" });
        }
    }
    public static class SDL_JoyHatEvent_byvalue extends SDL_JoyHatEvent implements Structure.ByValue {}
    public static class SDL_JoyHatEvent_byref extends SDL_JoyHatEvent implements Structure.ByReference {}
    public static class SDL_JoyButtonEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int which ;
        public byte button ;
        public byte state ;
        public byte padding1 ;
        public byte padding2 ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","which","button","state","padding1","padding2" });
        }
    }
    public static class SDL_JoyButtonEvent_byvalue extends SDL_JoyButtonEvent implements Structure.ByValue {}
    public static class SDL_JoyButtonEvent_byref extends SDL_JoyButtonEvent implements Structure.ByReference {}
    public static class SDL_JoyDeviceEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int which ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","which" });
        }
    }
    public static class SDL_JoyDeviceEvent_byvalue extends SDL_JoyDeviceEvent implements Structure.ByValue {}
    public static class SDL_JoyDeviceEvent_byref extends SDL_JoyDeviceEvent implements Structure.ByReference {}
    public static class SDL_ControllerAxisEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int which ;
        public byte axis ;
        public byte padding1 ;
        public byte padding2 ;
        public byte padding3 ;
        public short value ;
        public short padding4 ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","which","axis","padding1","padding2","padding3","value","padding4" });
        }
    }
    public static class SDL_ControllerAxisEvent_byvalue extends SDL_ControllerAxisEvent implements Structure.ByValue {}
    public static class SDL_ControllerAxisEvent_byref extends SDL_ControllerAxisEvent implements Structure.ByReference {}
    public static class SDL_ControllerButtonEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int which ;
        public byte button ;
        public byte state ;
        public byte padding1 ;
        public byte padding2 ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","which","button","state","padding1","padding2" });
        }
    }
    public static class SDL_ControllerButtonEvent_byvalue extends SDL_ControllerButtonEvent implements Structure.ByValue {}
    public static class SDL_ControllerButtonEvent_byref extends SDL_ControllerButtonEvent implements Structure.ByReference {}
    public static class SDL_ControllerDeviceEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int which ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","which" });
        }
    }
    public static class SDL_ControllerDeviceEvent_byvalue extends SDL_ControllerDeviceEvent implements Structure.ByValue {}
    public static class SDL_ControllerDeviceEvent_byref extends SDL_ControllerDeviceEvent implements Structure.ByReference {}
    public static class SDL_TouchFingerEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public long touchId ;
        public long fingerId ;
        public float x ;
        public float y ;
        public float dx ;
        public float dy ;
        public float pressure ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","touchId","fingerId","x","y","dx","dy","pressure" });
        }
    }
    public static class SDL_TouchFingerEvent_byvalue extends SDL_TouchFingerEvent implements Structure.ByValue {}
    public static class SDL_TouchFingerEvent_byref extends SDL_TouchFingerEvent implements Structure.ByReference {}
    public static class SDL_MultiGestureEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public long touchId ;
        public float dTheta ;
        public float dDist ;
        public float x ;
        public float y ;
        public short numFingers ;
        public short padding ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","touchId","dTheta","dDist","x","y","numFingers","padding" });
        }
    }
    public static class SDL_MultiGestureEvent_byvalue extends SDL_MultiGestureEvent implements Structure.ByValue {}
    public static class SDL_MultiGestureEvent_byref extends SDL_MultiGestureEvent implements Structure.ByReference {}
    public static class SDL_DollarGestureEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public long touchId ;
        public long gestureId ;
        public int numFingers ;
        public float error ;
        public float x ;
        public float y ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","touchId","gestureId","numFingers","error","x","y" });
        }
    }
    public static class SDL_DollarGestureEvent_byvalue extends SDL_DollarGestureEvent implements Structure.ByValue {}
    public static class SDL_DollarGestureEvent_byref extends SDL_DollarGestureEvent implements Structure.ByReference {}
    public static class SDL_DropEvent extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_DropEvent_byvalue extends SDL_DropEvent implements Structure.ByValue {}
    public static class SDL_DropEvent_byref extends SDL_DropEvent implements Structure.ByReference {}
    public static class SDL_QuitEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp" });
        }
    }
    public static class SDL_QuitEvent_byvalue extends SDL_QuitEvent implements Structure.ByValue {}
    public static class SDL_QuitEvent_byref extends SDL_QuitEvent implements Structure.ByReference {}
    public static class SDL_OSEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp" });
        }
    }
    public static class SDL_OSEvent_byvalue extends SDL_OSEvent implements Structure.ByValue {}
    public static class SDL_OSEvent_byref extends SDL_OSEvent implements Structure.ByReference {}
    public static class SDL_UserEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public int windowID ;
        public int code ;
        public Pointer data1 ;
        public Pointer data2 ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","windowID","code","data1","data2" });
        }
    }
    public static class SDL_UserEvent_byvalue extends SDL_UserEvent implements Structure.ByValue {}
    public static class SDL_UserEvent_byref extends SDL_UserEvent implements Structure.ByReference {}
    public static class SDL_SysWMmsg extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_SysWMmsg_byvalue extends SDL_SysWMmsg implements Structure.ByValue {}
    public static class SDL_SysWMmsg_byref extends SDL_SysWMmsg implements Structure.ByReference {}
    public static class SDL_SysWMEvent  extends Structure {
        public int type ;
        public int timestamp ;
        public Pointer msg ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","timestamp","msg" });
        }
    }
    public static class SDL_SysWMEvent_byvalue extends SDL_SysWMEvent implements Structure.ByValue {}
    public static class SDL_SysWMEvent_byref extends SDL_SysWMEvent implements Structure.ByReference {}
    public static class SDL_Event  extends Union {
        public int type ;
        public SDL_CommonEvent common ;
        public SDL_WindowEvent window ;
        public SDL_KeyboardEvent key ;
        public SDL_TextEditingEvent edit ;
        public SDL_TextInputEvent text ;
        public SDL_MouseMotionEvent motion ;
        public SDL_MouseButtonEvent button ;
        public SDL_MouseWheelEvent wheel ;
        public SDL_JoyAxisEvent jaxis ;
        public SDL_JoyBallEvent jball ;
        public SDL_JoyHatEvent jhat ;
        public SDL_JoyButtonEvent jbutton ;
        public SDL_JoyDeviceEvent jdevice ;
        public SDL_ControllerAxisEvent caxis ;
        public SDL_ControllerButtonEvent cbutton ;
        public SDL_ControllerDeviceEvent cdevice ;
        public SDL_QuitEvent quit ;
        public SDL_UserEvent user ;
        public SDL_SysWMEvent syswm ;
        public SDL_TouchFingerEvent tfinger ;
        public SDL_MultiGestureEvent mgesture ;
        public SDL_DollarGestureEvent dgesture ;
        public SDL_DropEvent drop ;
        public byte[] padding =new byte [56];
    }
    public static class SDL_Haptic extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_Haptic_byvalue extends SDL_Haptic implements Structure.ByValue {}
    public static class SDL_Haptic_byref extends SDL_Haptic implements Structure.ByReference {}
    public static class SDL_HapticDirection  extends Structure {
        public byte type ;
        public int[] dir =new int [3];
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","dir" });
        }
    }
    public static class SDL_HapticDirection_byvalue extends SDL_HapticDirection implements Structure.ByValue {}
    public static class SDL_HapticDirection_byref extends SDL_HapticDirection implements Structure.ByReference {}
    public static class SDL_HapticConstant  extends Structure {
        public short type ;
        public SDL_HapticDirection direction ;
        public int length ;
        public short delay ;
        public short button ;
        public short interval ;
        public short level ;
        public short attack_length ;
        public short attack_level ;
        public short fade_length ;
        public short fade_level ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","direction","length","delay","button","interval","level","attack_length","attack_level","fade_length","fade_level" });
        }
    }
    public static class SDL_HapticConstant_byvalue extends SDL_HapticConstant implements Structure.ByValue {}
    public static class SDL_HapticConstant_byref extends SDL_HapticConstant implements Structure.ByReference {}
    public static class SDL_HapticPeriodic  extends Structure {
        public short type ;
        public SDL_HapticDirection direction ;
        public int length ;
        public short delay ;
        public short button ;
        public short interval ;
        public short period ;
        public short magnitude ;
        public short offset ;
        public short phase ;
        public short attack_length ;
        public short attack_level ;
        public short fade_length ;
        public short fade_level ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","direction","length","delay","button","interval","period","magnitude","offset","phase","attack_length","attack_level","fade_length","fade_level" });
        }
    }
    public static class SDL_HapticPeriodic_byvalue extends SDL_HapticPeriodic implements Structure.ByValue {}
    public static class SDL_HapticPeriodic_byref extends SDL_HapticPeriodic implements Structure.ByReference {}
    public static class SDL_HapticCondition  extends Structure {
        public short type ;
        public SDL_HapticDirection direction ;
        public int length ;
        public short delay ;
        public short button ;
        public short interval ;
        public short[] right_sat =new short [3];
        public short[] left_sat =new short [3];
        public short[] right_coeff =new short [3];
        public short[] left_coeff =new short [3];
        public short[] deadband =new short [3];
        public short[] center =new short [3];
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","direction","length","delay","button","interval","right_sat","left_sat","right_coeff","left_coeff","deadband","center" });
        }
    }
    public static class SDL_HapticCondition_byvalue extends SDL_HapticCondition implements Structure.ByValue {}
    public static class SDL_HapticCondition_byref extends SDL_HapticCondition implements Structure.ByReference {}
    public static class SDL_HapticRamp  extends Structure {
        public short type ;
        public SDL_HapticDirection direction ;
        public int length ;
        public short delay ;
        public short button ;
        public short interval ;
        public short start ;
        public short end ;
        public short attack_length ;
        public short attack_level ;
        public short fade_length ;
        public short fade_level ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","direction","length","delay","button","interval","start","end","attack_length","attack_level","fade_length","fade_level" });
        }
    }
    public static class SDL_HapticRamp_byvalue extends SDL_HapticRamp implements Structure.ByValue {}
    public static class SDL_HapticRamp_byref extends SDL_HapticRamp implements Structure.ByReference {}
    public static class SDL_HapticLeftRight  extends Structure {
        public short type ;
        public int length ;
        public short large_magnitude ;
        public short small_magnitude ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "type","length","large_magnitude","small_magnitude" });
        }
    }
    public static class SDL_HapticLeftRight_byvalue extends SDL_HapticLeftRight implements Structure.ByValue {}
    public static class SDL_HapticLeftRight_byref extends SDL_HapticLeftRight implements Structure.ByReference {}
    public static class SDL_HapticCustom extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_HapticCustom_byvalue extends SDL_HapticCustom implements Structure.ByValue {}
    public static class SDL_HapticCustom_byref extends SDL_HapticCustom implements Structure.ByReference {}
    public static class SDL_HapticEffect  extends Union {
        public short type ;
        public SDL_HapticConstant ant ;
        public SDL_HapticPeriodic periodic ;
        public SDL_HapticCondition condition ;
        public SDL_HapticRamp ramp ;
        public SDL_HapticLeftRight leftright ;
        public SDL_HapticCustom custom ;
    }
    public static class SDL_atomic_t  extends Structure {
        public int value ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "value" });
        }
    }
    public static class SDL_atomic_t_byvalue extends SDL_atomic_t implements Structure.ByValue {}
    public static class SDL_atomic_t_byref extends SDL_atomic_t implements Structure.ByReference {}
    public static class SDL_GameController extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_GameController_byvalue extends SDL_GameController implements Structure.ByValue {}
    public static class SDL_GameController_byref extends SDL_GameController implements Structure.ByReference {}
    public static class SDL_GameControllerButtonBind extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_GameControllerButtonBind_byvalue extends SDL_GameControllerButtonBind implements Structure.ByValue {}
    public static class SDL_GameControllerButtonBind_byref extends SDL_GameControllerButtonBind implements Structure.ByReference {}
    public static class SDL_Finger  extends Structure {
        public long id ;
        public float x ;
        public float y ;
        public float pressure ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "id","x","y","pressure" });
        }
    }
    public static class SDL_Finger_byvalue extends SDL_Finger implements Structure.ByValue {}
    public static class SDL_Finger_byref extends SDL_Finger implements Structure.ByReference {}
    public static class GLsync extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class GLsync_byvalue extends GLsync implements Structure.ByValue {}
    public static class GLsync_byref extends GLsync implements Structure.ByReference {}
    public static class SDL_Cursor extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_Cursor_byvalue extends SDL_Cursor implements Structure.ByValue {}
    public static class SDL_Cursor_byref extends SDL_Cursor implements Structure.ByReference {}
    public static class SDL_MessageBoxButtonData extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_MessageBoxButtonData_byvalue extends SDL_MessageBoxButtonData implements Structure.ByValue {}
    public static class SDL_MessageBoxButtonData_byref extends SDL_MessageBoxButtonData implements Structure.ByReference {}
    public static class SDL_MessageBoxColor  extends Structure {
        public byte r ;
        public byte g ;
        public byte b ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "r","g","b" });
        }
    }
    public static class SDL_MessageBoxColor_byvalue extends SDL_MessageBoxColor implements Structure.ByValue {}
    public static class SDL_MessageBoxColor_byref extends SDL_MessageBoxColor implements Structure.ByReference {}
    public static class SDL_MessageBoxColorScheme  extends Structure {
        public SDL_MessageBoxColor[] colors =new SDL_MessageBoxColor [SDL_MESSAGEBOX_COLOR_MAX];
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "colors" });
        }
    }
    public static class SDL_MessageBoxColorScheme_byvalue extends SDL_MessageBoxColorScheme implements Structure.ByValue {}
    public static class SDL_MessageBoxColorScheme_byref extends SDL_MessageBoxColorScheme implements Structure.ByReference {}
    public static class SDL_MessageBoxData extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_MessageBoxData_byvalue extends SDL_MessageBoxData implements Structure.ByValue {}
    public static class SDL_MessageBoxData_byref extends SDL_MessageBoxData implements Structure.ByReference {}
    public static class SDL_Point  extends Structure {
        public int x ;
        public int y ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "x","y" });
        }
    }
    public static class SDL_Point_byvalue extends SDL_Point implements Structure.ByValue {}
    public static class SDL_Point_byref extends SDL_Point implements Structure.ByReference {}
    public static class SDL_Rect  extends Structure {
        public int x ;
        public int y ;
        public int w ;
        public int h ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "x","y","w","h" });
        }
    }
    public static class SDL_Rect_byvalue extends SDL_Rect implements Structure.ByValue {}
    public static class SDL_Rect_byref extends SDL_Rect implements Structure.ByReference {}
    public static class SDL_Color  extends Structure {
        public byte r ;
        public byte g ;
        public byte b ;
        public byte a ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "r","g","b","a" });
        }
    }
    public static class SDL_Color_byvalue extends SDL_Color implements Structure.ByValue {}
    public static class SDL_Color_byref extends SDL_Color implements Structure.ByReference {}
    public static class SDL_Palette  extends Structure {
        public int ncolors ;
        public Pointer colors ;
        public int version ;
        public int refcount ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "ncolors","colors","version","refcount" });
        }
    }
    public static class SDL_Palette_byvalue extends SDL_Palette implements Structure.ByValue {}
    public static class SDL_Palette_byref extends SDL_Palette implements Structure.ByReference {}
    public static class SDL_PixelFormat  extends Structure {
        public int format ;
        public Pointer palette ;
        public byte BitsPerPixel ;
        public byte BytesPerPixel ;
        public byte[] padding =new byte [2];
        public int Rmask ;
        public int Gmask ;
        public int Bmask ;
        public int Amask ;
        public byte Rloss ;
        public byte Gloss ;
        public byte Bloss ;
        public byte Aloss ;
        public byte Rshift ;
        public byte Gshift ;
        public byte Bshift ;
        public byte Ashift ;
        public int refcount ;
        public Pointer next ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "format","palette","BitsPerPixel","BytesPerPixel","padding","Rmask","Gmask","Bmask","Amask","Rloss","Gloss","Bloss","Aloss","Rshift","Gshift","Bshift","Ashift","refcount","next" });
        }
    }
    public static class SDL_PixelFormat_byvalue extends SDL_PixelFormat implements Structure.ByValue {}
    public static class SDL_PixelFormat_byref extends SDL_PixelFormat implements Structure.ByReference {}
    public static class SDL_DisplayMode  extends Structure {
        public int format ;
        public int w ;
        public int h ;
        public int refresh_rate ;
        public Pointer driverdata ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "format","w","h","refresh_rate","driverdata" });
        }
    }
    public static class SDL_DisplayMode_byvalue extends SDL_DisplayMode implements Structure.ByValue {}
    public static class SDL_DisplayMode_byref extends SDL_DisplayMode implements Structure.ByReference {}
    public static class SDL_Window extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_Window_byvalue extends SDL_Window implements Structure.ByValue {}
    public static class SDL_Window_byref extends SDL_Window implements Structure.ByReference {}
    public static class SDL_Thread extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_Thread_byvalue extends SDL_Thread implements Structure.ByValue {}
    public static class SDL_Thread_byref extends SDL_Thread implements Structure.ByReference {}
    public static class SDL_RWops extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_RWops_byvalue extends SDL_RWops implements Structure.ByValue {}
    public static class SDL_RWops_byref extends SDL_RWops implements Structure.ByReference {}
    public static class SDL_RendererInfo extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_RendererInfo_byvalue extends SDL_RendererInfo implements Structure.ByValue {}
    public static class SDL_RendererInfo_byref extends SDL_RendererInfo implements Structure.ByReference {}
    public static class SDL_Renderer extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_Renderer_byvalue extends SDL_Renderer implements Structure.ByValue {}
    public static class SDL_Renderer_byref extends SDL_Renderer implements Structure.ByReference {}
    public static class SDL_Texture extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_Texture_byvalue extends SDL_Texture implements Structure.ByValue {}
    public static class SDL_Texture_byref extends SDL_Texture implements Structure.ByReference {}
    public static class SDL_iconv_t extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_iconv_t_byvalue extends SDL_iconv_t implements Structure.ByValue {}
    public static class SDL_iconv_t_byref extends SDL_iconv_t implements Structure.ByReference {}
    public static class SDL_version  extends Structure {
        public byte major ;
        public byte minor ;
        public byte patch ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "major","minor","patch" });
        }
    }
    public static class SDL_version_byvalue extends SDL_version implements Structure.ByValue {}
    public static class SDL_version_byref extends SDL_version implements Structure.ByReference {}
    public static class SDL_Surface  extends Structure {
        public int flags ;
        public Pointer format ;
        public int w ;
        public int h ;
        public int pitch ;
        public Pointer pixels ;
        public Pointer userdata ;
        public int locked ;
        public Pointer lock_data ;
        public SDL_Rect clip_rect ;
        public Pointer map ;
        public int refcount ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "flags","format","w","h","pitch","pixels","userdata","locked","lock_data","clip_rect","map","refcount" });
        }
    }
    public static class SDL_Surface_byvalue extends SDL_Surface implements Structure.ByValue {}
    public static class SDL_Surface_byref extends SDL_Surface implements Structure.ByReference {}
    public static class SDL_Joystick extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_Joystick_byvalue extends SDL_Joystick implements Structure.ByValue {}
    public static class SDL_Joystick_byref extends SDL_Joystick implements Structure.ByReference {}
    public static class SDL_JoystickGUID  extends Structure {
        public byte[] data =new byte [16];
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "data" });
        }
    }
    public static class SDL_JoystickGUID_byvalue extends SDL_JoystickGUID implements Structure.ByValue {}
    public static class SDL_JoystickGUID_byref extends SDL_JoystickGUID implements Structure.ByReference {}
    public static class SDL_Keysym  extends Structure {
        public int scancode ;
        public int sym ;
        public short mod ;
        public int unused ;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "scancode","sym","mod","unused" });
        }
    }
    public static class SDL_Keysym_byvalue extends SDL_Keysym implements Structure.ByValue {}
    public static class SDL_Keysym_byref extends SDL_Keysym implements Structure.ByReference {}
    public static class SDL_mutex extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_mutex_byvalue extends SDL_mutex implements Structure.ByValue {}
    public static class SDL_mutex_byref extends SDL_mutex implements Structure.ByReference {}
    public static class SDL_sem extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_sem_byvalue extends SDL_sem implements Structure.ByValue {}
    public static class SDL_sem_byref extends SDL_sem implements Structure.ByReference {}
    public static class SDL_cond extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_cond_byvalue extends SDL_cond implements Structure.ByValue {}
    public static class SDL_cond_byref extends SDL_cond implements Structure.ByReference {}
    public static class NSWindow extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class NSWindow_byvalue extends NSWindow implements Structure.ByValue {}
    public static class NSWindow_byref extends NSWindow implements Structure.ByReference {}
    public static class UIWindow extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class UIWindow_byvalue extends UIWindow implements Structure.ByValue {}
    public static class UIWindow_byref extends UIWindow implements Structure.ByReference {}
    public static class UIViewController extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class UIViewController_byvalue extends UIViewController implements Structure.ByValue {}
    public static class UIViewController_byref extends UIViewController implements Structure.ByReference {}
    public static class SDL_SysWMinfo extends Structure {
        public int dummy;
        public List<String> getFieldOrder(){
            return Arrays.asList(new String[]{ "dummy" });
        }
    }
    public static class SDL_SysWMinfo_byvalue extends SDL_SysWMinfo implements Structure.ByValue {}
    public static class SDL_SysWMinfo_byref extends SDL_SysWMinfo implements Structure.ByReference {}
    //functions
    public static native int SDL_SetClipboardText (String text);
    public static native String SDL_GetClipboardText ();
    public static native int SDL_HasClipboardText ();
    public static native SDL_Window_byref SDL_CreateShapedWindow (String title,int x,int y,int w,int h,int flags);
    public static native int SDL_IsShapedWindow (SDL_Window window);
    public static native int SDL_SetWindowShape (SDL_Window window,SDL_Surface shape,SDL_WindowShapeMode shape_mode);
    public static native int SDL_GetShapedWindowMode (SDL_Window window,SDL_WindowShapeMode shape_mode);
    public static native int SDL_GetNumAudioDrivers ();
    public static native int SDL_AudioInit (String driver_name);
    public static native void SDL_AudioQuit ();
    public static native int SDL_OpenAudio (SDL_AudioSpec desired,SDL_AudioSpec obtained);
    public static native int SDL_GetNumAudioDevices (int iscapture);
    public static native int SDL_OpenAudioDevice (String device,int iscapture,SDL_AudioSpec desired,SDL_AudioSpec obtained,int allowed_changes);
    public static native int SDL_GetAudioStatus ();
    public static native int SDL_GetAudioDeviceStatus (int dev);
    public static native void SDL_PauseAudio (int pause_on);
    public static native void SDL_PauseAudioDevice (int dev,int pause_on);
    public static native SDL_AudioSpec_byref SDL_LoadWAV_RW (SDL_RWops src,int freesrc,SDL_AudioSpec spec,Pointer audio_buf,Pointer audio_len);
    public static native void SDL_FreeWAV (Pointer audio_buf);
    public static native int SDL_BuildAudioCVT (SDL_AudioCVT cvt,short src_format,byte src_channels,int src_rate,short dst_format,byte dst_channels,int dst_rate);
    public static native int SDL_ConvertAudio (SDL_AudioCVT cvt);
    public static native void SDL_MixAudio (Pointer dst,Pointer src,int len,int volume);
    public static native void SDL_MixAudioFormat (Pointer dst,Pointer src,short format,int len,int volume);
    public static native void SDL_LockAudio ();
    public static native void SDL_LockAudioDevice (int dev);
    public static native void SDL_UnlockAudio ();
    public static native void SDL_UnlockAudioDevice (int dev);
    public static native void SDL_CloseAudio ();
    public static native void SDL_CloseAudioDevice (int dev);
    public static native void SDL_PumpEvents ();
    public static native int SDL_PeepEvents (SDL_Event events,int numevents,int action,int minType,int maxType);
    public static native int SDL_HasEvent (int type);
    public static native int SDL_HasEvents (int minType,int maxType);
    public static native void SDL_FlushEvent (int type);
    public static native void SDL_FlushEvents (int minType,int maxType);
    public static native int SDL_PollEvent (SDL_Event event);
    public static native int SDL_WaitEvent (SDL_Event event);
    public static native int SDL_WaitEventTimeout (SDL_Event event,int timeout);
    public static native int SDL_PushEvent (SDL_Event event);
    public static native void SDL_SetEventFilter (Function filter,Pointer userdata);
    public static native int SDL_GetEventFilter (Pointer filter,Pointer userdata);
    public static native void SDL_AddEventWatch (Function filter,Pointer userdata);
    public static native void SDL_DelEventWatch (Function filter,Pointer userdata);
    public static native void SDL_FilterEvents (Function filter,Pointer userdata);
    public static native byte SDL_EventState (int type,int state);
    public static native int SDL_RegisterEvents (int numevents);
    public static native int SDL_NumHaptics ();
    public static native SDL_Haptic_byref SDL_HapticOpen (int device_index);
    public static native int SDL_HapticOpened (int device_index);
    public static native int SDL_HapticIndex (SDL_Haptic haptic);
    public static native int SDL_MouseIsHaptic ();
    public static native SDL_Haptic_byref SDL_HapticOpenFromMouse ();
    public static native int SDL_JoystickIsHaptic (SDL_Joystick joystick);
    public static native SDL_Haptic_byref SDL_HapticOpenFromJoystick (SDL_Joystick joystick);
    public static native void SDL_HapticClose (SDL_Haptic haptic);
    public static native int SDL_HapticNumEffects (SDL_Haptic haptic);
    public static native int SDL_HapticNumEffectsPlaying (SDL_Haptic haptic);
    public static native int SDL_HapticNumAxes (SDL_Haptic haptic);
    public static native int SDL_HapticEffectSupported (SDL_Haptic haptic,SDL_HapticEffect effect);
    public static native int SDL_HapticNewEffect (SDL_Haptic haptic,SDL_HapticEffect effect);
    public static native int SDL_HapticUpdateEffect (SDL_Haptic haptic,int effect,SDL_HapticEffect data);
    public static native int SDL_HapticRunEffect (SDL_Haptic haptic,int effect,int iterations);
    public static native int SDL_HapticStopEffect (SDL_Haptic haptic,int effect);
    public static native void SDL_HapticDestroyEffect (SDL_Haptic haptic,int effect);
    public static native int SDL_HapticGetEffectStatus (SDL_Haptic haptic,int effect);
    public static native int SDL_HapticSetGain (SDL_Haptic haptic,int gain);
    public static native int SDL_HapticSetAutocenter (SDL_Haptic haptic,int autocenter);
    public static native int SDL_HapticPause (SDL_Haptic haptic);
    public static native int SDL_HapticUnpause (SDL_Haptic haptic);
    public static native int SDL_HapticStopAll (SDL_Haptic haptic);
    public static native int SDL_HapticRumbleSupported (SDL_Haptic haptic);
    public static native int SDL_HapticRumbleInit (SDL_Haptic haptic);
    public static native int SDL_HapticRumblePlay (SDL_Haptic haptic,float strength,int length);
    public static native int SDL_HapticRumbleStop (SDL_Haptic haptic);
    public static native int SDL_AtomicTryLock (Pointer lock);
    public static native void SDL_AtomicLock (Pointer lock);
    public static native void SDL_AtomicUnlock (Pointer lock);
    public static native int SDL_AtomicCAS (SDL_atomic_t a,int oldval,int newval);
    public static native int SDL_AtomicSet (SDL_atomic_t a,int v);
    public static native int SDL_AtomicGet (SDL_atomic_t a);
    public static native int SDL_AtomicAdd (SDL_atomic_t a,int v);
    public static native int SDL_AtomicCASPtr (Pointer a,Pointer oldval,Pointer newval);
    public static native Pointer SDL_AtomicSetPtr (Pointer a,Pointer v);
    public static native Pointer SDL_AtomicGetPtr (Pointer a);
    public static native int SDL_GameControllerAddMappingsFromRW (SDL_RWops rw,int freerw);
    public static native int SDL_GameControllerAddMapping (String mappingString);
    public static native String SDL_GameControllerMappingForGUID (SDL_JoystickGUID_byvalue guid);
    public static native String SDL_GameControllerMapping (SDL_GameController gamecontroller);
    public static native int SDL_IsGameController (int joystick_index);
    public static native SDL_GameController_byref SDL_GameControllerOpen (int joystick_index);
    public static native int SDL_GameControllerGetAttached (SDL_GameController gamecontroller);
    public static native SDL_Joystick_byref SDL_GameControllerGetJoystick (SDL_GameController gamecontroller);
    public static native int SDL_GameControllerEventState (int state);
    public static native void SDL_GameControllerUpdate ();
    public static native int SDL_GameControllerGetAxisFromString (String pchString);
    public static native SDL_GameControllerButtonBind_byvalue SDL_GameControllerGetBindForAxis (SDL_GameController gamecontroller,int axis);
    public static native short SDL_GameControllerGetAxis (SDL_GameController gamecontroller,int axis);
    public static native int SDL_GameControllerGetButtonFromString (String pchString);
    public static native SDL_GameControllerButtonBind_byvalue SDL_GameControllerGetBindForButton (SDL_GameController gamecontroller,int button);
    public static native byte SDL_GameControllerGetButton (SDL_GameController gamecontroller,int button);
    public static native void SDL_GameControllerClose (SDL_GameController gamecontroller);
    public static native int SDL_RecordGesture (long touchId);
    public static native int SDL_SaveAllDollarTemplates (SDL_RWops dst);
    public static native int SDL_SaveDollarTemplate (long gestureId,SDL_RWops dst);
    public static native int SDL_LoadDollarTemplates (long touchId,SDL_RWops src);
    public static native int SDL_GetTicks ();
    public static native long SDL_GetPerformanceCounter ();
    public static native long SDL_GetPerformanceFrequency ();
    public static native void SDL_Delay (int ms);
    public static native void SDL_LogSetAllPriority (int priority);
    public static native void SDL_LogSetPriority (int category,int priority);
    public static native int SDL_LogGetPriority (int category);
    public static native int SDL_GetNumTouchDevices ();
    public static native long SDL_GetTouchDevice (int index);
    public static native int SDL_GetNumTouchFingers (long touchID);
    public static native SDL_Finger_byref SDL_GetTouchFinger (long touchID,int index);
    public static native SDL_Window_byref SDL_GetMouseFocus ();
    public static native int SDL_GetMouseState (Pointer x,Pointer y);
    public static native int SDL_GetRelativeMouseState (Pointer x,Pointer y);
    public static native void SDL_WarpMouseInWindow (SDL_Window window,int x,int y);
    public static native int SDL_SetRelativeMouseMode (int enabled);
    public static native int SDL_GetRelativeMouseMode ();
    public static native SDL_Cursor_byref SDL_CreateCursor (Pointer data,Pointer mask,int w,int h,int hot_x,int hot_y);
    public static native SDL_Cursor_byref SDL_CreateColorCursor (SDL_Surface surface,int hot_x,int hot_y);
    public static native SDL_Cursor_byref SDL_CreateSystemCursor (int id);
    public static native void SDL_SetCursor (SDL_Cursor cursor);
    public static native SDL_Cursor_byref SDL_GetCursor ();
    public static native SDL_Cursor_byref SDL_GetDefaultCursor ();
    public static native void SDL_FreeCursor (SDL_Cursor cursor);
    public static native int SDL_ShowCursor (int toggle);
    public static native int SDL_Init (int flags);
    public static native int SDL_InitSubSystem (int flags);
    public static native void SDL_QuitSubSystem (int flags);
    public static native int SDL_WasInit (int flags);
    public static native void SDL_Quit ();
    public static native void SDL_ClearError ();
    public static native int SDL_Error (int code);
    public static native int SDL_ShowMessageBox (SDL_MessageBoxData messageboxdata,Pointer buttonid);
    public static native int SDL_ShowSimpleMessageBox (int flags,String title,String message,SDL_Window window);
    public static native int SDL_HasIntersection (SDL_Rect A,SDL_Rect B);
    public static native int SDL_IntersectRect (SDL_Rect A,SDL_Rect B,SDL_Rect result);
    public static native void SDL_UnionRect (SDL_Rect A,SDL_Rect B,SDL_Rect result);
    public static native int SDL_EnclosePoints (SDL_Point points,int count,SDL_Rect clip,SDL_Rect result);
    public static native int SDL_IntersectRectAndLine (SDL_Rect rect,Pointer X1,Pointer Y1,Pointer X2,Pointer Y2);
    public static native int SDL_GetCPUCount ();
    public static native int SDL_GetCPUCacheLineSize ();
    public static native int SDL_HasRDTSC ();
    public static native int SDL_HasAltiVec ();
    public static native int SDL_HasMMX ();
    public static native int SDL_Has3DNow ();
    public static native int SDL_HasSSE ();
    public static native int SDL_HasSSE2 ();
    public static native int SDL_HasSSE3 ();
    public static native int SDL_HasSSE41 ();
    public static native int SDL_HasSSE42 ();
    public static native int SDL_HasAVX ();
    public static native int SDL_GetSystemRAM ();
    public static native int SDL_PixelFormatEnumToMasks (int format,Pointer bpp,Pointer Rmask,Pointer Gmask,Pointer Bmask,Pointer Amask);
    public static native int SDL_MasksToPixelFormatEnum (int bpp,int Rmask,int Gmask,int Bmask,int Amask);
    public static native SDL_PixelFormat_byref SDL_AllocFormat (int pixel_format);
    public static native void SDL_FreeFormat (SDL_PixelFormat format);
    public static native SDL_Palette_byref SDL_AllocPalette (int ncolors);
    public static native int SDL_SetPixelFormatPalette (SDL_PixelFormat format,SDL_Palette palette);
    public static native int SDL_SetPaletteColors (SDL_Palette palette,SDL_Color colors,int firstcolor,int ncolors);
    public static native void SDL_FreePalette (SDL_Palette palette);
    public static native int SDL_MapRGB (SDL_PixelFormat format,byte r,byte g,byte b);
    public static native int SDL_MapRGBA (SDL_PixelFormat format,byte r,byte g,byte b,byte a);
    public static native void SDL_GetRGB (int pixel,SDL_PixelFormat format,Pointer r,Pointer g,Pointer b);
    public static native void SDL_GetRGBA (int pixel,SDL_PixelFormat format,Pointer r,Pointer g,Pointer b,Pointer a);
    public static native void SDL_CalculateGammaRamp (float gamma,Pointer ramp);
    public static native int SDL_GetNumVideoDrivers ();
    public static native int SDL_VideoInit (String driver_name);
    public static native void SDL_VideoQuit ();
    public static native int SDL_GetNumVideoDisplays ();
    public static native int SDL_GetDisplayBounds (int displayIndex,SDL_Rect rect);
    public static native int SDL_GetNumDisplayModes (int displayIndex);
    public static native int SDL_GetDisplayMode (int displayIndex,int modeIndex,SDL_DisplayMode mode);
    public static native int SDL_GetDesktopDisplayMode (int displayIndex,SDL_DisplayMode mode);
    public static native int SDL_GetCurrentDisplayMode (int displayIndex,SDL_DisplayMode mode);
    public static native SDL_DisplayMode_byref SDL_GetClosestDisplayMode (int displayIndex,SDL_DisplayMode mode,SDL_DisplayMode closest);
    public static native int SDL_GetWindowDisplayIndex (SDL_Window window);
    public static native int SDL_SetWindowDisplayMode (SDL_Window window,SDL_DisplayMode mode);
    public static native int SDL_GetWindowDisplayMode (SDL_Window window,SDL_DisplayMode mode);
    public static native int SDL_GetWindowPixelFormat (SDL_Window window);
    public static native SDL_Window_byref SDL_CreateWindow (String title,int x,int y,int w,int h,int flags);
    public static native SDL_Window_byref SDL_CreateWindowFrom (Pointer data);
    public static native int SDL_GetWindowID (SDL_Window window);
    public static native SDL_Window_byref SDL_GetWindowFromID (int id);
    public static native int SDL_GetWindowFlags (SDL_Window window);
    public static native void SDL_SetWindowTitle (SDL_Window window,String title);
    public static native void SDL_SetWindowIcon (SDL_Window window,SDL_Surface icon);
    public static native Pointer SDL_SetWindowData (SDL_Window window,String name,Pointer userdata);
    public static native Pointer SDL_GetWindowData (SDL_Window window,String name);
    public static native void SDL_SetWindowPosition (SDL_Window window,int x,int y);
    public static native void SDL_GetWindowPosition (SDL_Window window,Pointer x,Pointer y);
    public static native void SDL_SetWindowSize (SDL_Window window,int w,int h);
    public static native void SDL_GetWindowSize (SDL_Window window,Pointer w,Pointer h);
    public static native void SDL_SetWindowMinimumSize (SDL_Window window,int min_w,int min_h);
    public static native void SDL_GetWindowMinimumSize (SDL_Window window,Pointer w,Pointer h);
    public static native void SDL_SetWindowMaximumSize (SDL_Window window,int max_w,int max_h);
    public static native void SDL_GetWindowMaximumSize (SDL_Window window,Pointer w,Pointer h);
    public static native void SDL_SetWindowBordered (SDL_Window window,int bordered);
    public static native void SDL_ShowWindow (SDL_Window window);
    public static native void SDL_HideWindow (SDL_Window window);
    public static native void SDL_RaiseWindow (SDL_Window window);
    public static native void SDL_MaximizeWindow (SDL_Window window);
    public static native void SDL_MinimizeWindow (SDL_Window window);
    public static native void SDL_RestoreWindow (SDL_Window window);
    public static native int SDL_SetWindowFullscreen (SDL_Window window,int flags);
    public static native SDL_Surface_byref SDL_GetWindowSurface (SDL_Window window);
    public static native int SDL_UpdateWindowSurface (SDL_Window window);
    public static native int SDL_UpdateWindowSurfaceRects (SDL_Window window,SDL_Rect rects,int numrects);
    public static native void SDL_SetWindowGrab (SDL_Window window,int grabbed);
    public static native int SDL_GetWindowGrab (SDL_Window window);
    public static native int SDL_SetWindowBrightness (SDL_Window window,float brightness);
    public static native float SDL_GetWindowBrightness (SDL_Window window);
    public static native int SDL_SetWindowGammaRamp (SDL_Window window,Pointer red,Pointer green,Pointer blue);
    public static native int SDL_GetWindowGammaRamp (SDL_Window window,Pointer red,Pointer green,Pointer blue);
    public static native void SDL_DestroyWindow (SDL_Window window);
    public static native int SDL_IsScreenSaverEnabled ();
    public static native void SDL_EnableScreenSaver ();
    public static native void SDL_DisableScreenSaver ();
    public static native int SDL_GL_LoadLibrary (String path);
    public static native Pointer SDL_GL_GetProcAddress (String proc);
    public static native void SDL_GL_UnloadLibrary ();
    public static native int SDL_GL_ExtensionSupported (String extension);
    public static native void SDL_GL_ResetAttributes ();
    public static native int SDL_GL_SetAttribute (int attr,int value);
    public static native int SDL_GL_GetAttribute (int attr,Pointer value);
    public static native Pointer SDL_GL_CreateContext (SDL_Window window);
    public static native int SDL_GL_MakeCurrent (SDL_Window window,Pointer context);
    public static native SDL_Window_byref SDL_GL_GetCurrentWindow ();
    public static native Pointer SDL_GL_GetCurrentContext ();
    public static native void SDL_GL_GetDrawableSize (SDL_Window window,Pointer w,Pointer h);
    public static native int SDL_GL_SetSwapInterval (int interval);
    public static native int SDL_GL_GetSwapInterval ();
    public static native void SDL_GL_SwapWindow (SDL_Window window);
    public static native void SDL_GL_DeleteContext (Pointer context);
    public static native int SDL_SetThreadPriority (int priority);
    public static native void SDL_WaitThread (SDL_Thread thread,Pointer status);
    public static native void SDL_DetachThread (SDL_Thread thread);
    public static native int SDL_TLSCreate ();
    public static native Pointer SDL_TLSGet (int id);
    public static native SDL_RWops_byref SDL_RWFromFile (String file,String mode);
    public static native SDL_RWops_byref SDL_RWFromMem (Pointer mem,int size);
    public static native SDL_RWops_byref SDL_RWFromConstMem (Pointer mem,int size);
    public static native SDL_RWops_byref SDL_AllocRW ();
    public static native void SDL_FreeRW (SDL_RWops area);
    public static native byte SDL_ReadU8 (SDL_RWops src);
    public static native short SDL_ReadLE16 (SDL_RWops src);
    public static native short SDL_ReadBE16 (SDL_RWops src);
    public static native int SDL_ReadLE32 (SDL_RWops src);
    public static native int SDL_ReadBE32 (SDL_RWops src);
    public static native long SDL_ReadLE64 (SDL_RWops src);
    public static native long SDL_ReadBE64 (SDL_RWops src);
    public static native long SDL_WriteU8 (SDL_RWops dst,byte value);
    public static native long SDL_WriteLE16 (SDL_RWops dst,short value);
    public static native long SDL_WriteBE16 (SDL_RWops dst,short value);
    public static native long SDL_WriteLE32 (SDL_RWops dst,int value);
    public static native long SDL_WriteBE32 (SDL_RWops dst,int value);
    public static native long SDL_WriteLE64 (SDL_RWops dst,long value);
    public static native long SDL_WriteBE64 (SDL_RWops dst,long value);
    public static native int SDL_GetNumRenderDrivers ();
    public static native int SDL_GetRenderDriverInfo (int index,SDL_RendererInfo info);
    public static native int SDL_CreateWindowAndRenderer (int width,int height,int window_flags,Pointer window,Pointer renderer);
    public static native SDL_Renderer_byref SDL_CreateRenderer (SDL_Window window,int index,int flags);
    public static native SDL_Renderer_byref SDL_CreateSoftwareRenderer (SDL_Surface surface);
    public static native SDL_Renderer_byref SDL_GetRenderer (SDL_Window window);
    public static native int SDL_GetRendererInfo (SDL_Renderer renderer,SDL_RendererInfo info);
    public static native int SDL_GetRendererOutputSize (SDL_Renderer renderer,Pointer w,Pointer h);
    public static native SDL_Texture_byref SDL_CreateTexture (SDL_Renderer renderer,int format,int access,int w,int h);
    public static native SDL_Texture_byref SDL_CreateTextureFromSurface (SDL_Renderer renderer,SDL_Surface surface);
    public static native int SDL_QueryTexture (SDL_Texture texture,Pointer format,Pointer access,Pointer w,Pointer h);
    public static native int SDL_SetTextureColorMod (SDL_Texture texture,byte r,byte g,byte b);
    public static native int SDL_GetTextureColorMod (SDL_Texture texture,Pointer r,Pointer g,Pointer b);
    public static native int SDL_SetTextureAlphaMod (SDL_Texture texture,byte alpha);
    public static native int SDL_GetTextureAlphaMod (SDL_Texture texture,Pointer alpha);
    public static native int SDL_SetTextureBlendMode (SDL_Texture texture,int blendMode);
    public static native int SDL_GetTextureBlendMode (SDL_Texture texture,Pointer blendMode);
    public static native int SDL_UpdateTexture (SDL_Texture texture,SDL_Rect rect,Pointer pixels,int pitch);
    public static native int SDL_UpdateYUVTexture (SDL_Texture texture,SDL_Rect rect,Pointer Yplane,int Ypitch,Pointer Uplane,int Upitch,Pointer Vplane,int Vpitch);
    public static native int SDL_LockTexture (SDL_Texture texture,SDL_Rect rect,Pointer pixels,Pointer pitch);
    public static native void SDL_UnlockTexture (SDL_Texture texture);
    public static native int SDL_RenderTargetSupported (SDL_Renderer renderer);
    public static native int SDL_SetRenderTarget (SDL_Renderer renderer,SDL_Texture texture);
    public static native SDL_Texture_byref SDL_GetRenderTarget (SDL_Renderer renderer);
    public static native int SDL_RenderSetLogicalSize (SDL_Renderer renderer,int w,int h);
    public static native void SDL_RenderGetLogicalSize (SDL_Renderer renderer,Pointer w,Pointer h);
    public static native int SDL_RenderSetViewport (SDL_Renderer renderer,SDL_Rect rect);
    public static native void SDL_RenderGetViewport (SDL_Renderer renderer,SDL_Rect rect);
    public static native int SDL_RenderSetClipRect (SDL_Renderer renderer,SDL_Rect rect);
    public static native void SDL_RenderGetClipRect (SDL_Renderer renderer,SDL_Rect rect);
    public static native int SDL_RenderSetScale (SDL_Renderer renderer,float scaleX,float scaleY);
    public static native void SDL_RenderGetScale (SDL_Renderer renderer,Pointer scaleX,Pointer scaleY);
    public static native int SDL_SetRenderDrawColor (SDL_Renderer renderer,byte r,byte g,byte b,byte a);
    public static native int SDL_GetRenderDrawColor (SDL_Renderer renderer,Pointer r,Pointer g,Pointer b,Pointer a);
    public static native int SDL_SetRenderDrawBlendMode (SDL_Renderer renderer,int blendMode);
    public static native int SDL_GetRenderDrawBlendMode (SDL_Renderer renderer,Pointer blendMode);
    public static native int SDL_RenderClear (SDL_Renderer renderer);
    public static native int SDL_RenderDrawPoint (SDL_Renderer renderer,int x,int y);
    public static native int SDL_RenderDrawPoints (SDL_Renderer renderer,SDL_Point points,int count);
    public static native int SDL_RenderDrawLine (SDL_Renderer renderer,int x1,int y1,int x2,int y2);
    public static native int SDL_RenderDrawLines (SDL_Renderer renderer,SDL_Point points,int count);
    public static native int SDL_RenderDrawRect (SDL_Renderer renderer,SDL_Rect rect);
    public static native int SDL_RenderDrawRects (SDL_Renderer renderer,SDL_Rect rects,int count);
    public static native int SDL_RenderFillRect (SDL_Renderer renderer,SDL_Rect rect);
    public static native int SDL_RenderFillRects (SDL_Renderer renderer,SDL_Rect rects,int count);
    public static native int SDL_RenderCopy (SDL_Renderer renderer,SDL_Texture texture,SDL_Rect srcrect,SDL_Rect dstrect);
    public static native int SDL_RenderCopyEx (SDL_Renderer renderer,SDL_Texture texture,SDL_Rect srcrect,SDL_Rect dstrect,double angle,SDL_Point center,int flip);
    public static native int SDL_RenderReadPixels (SDL_Renderer renderer,SDL_Rect rect,int format,Pointer pixels,int pitch);
    public static native void SDL_RenderPresent (SDL_Renderer renderer);
    public static native void SDL_DestroyTexture (SDL_Texture texture);
    public static native void SDL_DestroyRenderer (SDL_Renderer renderer);
    public static native int SDL_GL_BindTexture (SDL_Texture texture,Pointer texw,Pointer texh);
    public static native int SDL_GL_UnbindTexture (SDL_Texture texture);
    public static native String SDL_GetBasePath ();
    public static native String SDL_GetPrefPath (String org,String app);
    public static native void SDL_SetMainReady ();
    public static native Pointer SDL_malloc (long size);
    public static native Pointer SDL_calloc (long nmemb,long size);
    public static native Pointer SDL_realloc (Pointer mem,long size);
    public static native void SDL_free (Pointer mem);
    public static native String SDL_getenv (String name);
    public static native int SDL_setenv (String name,String value,int overwrite);
    public static native int SDL_abs (int x);
    public static native int SDL_isdigit (int x);
    public static native int SDL_isspace (int x);
    public static native int SDL_toupper (int x);
    public static native int SDL_tolower (int x);
    public static native Pointer SDL_memset (Pointer dst,int c,long len);
    public static native Pointer SDL_memcpy (Pointer dst,Pointer src,long len);
    public static native Pointer SDL_memmove (Pointer dst,Pointer src,long len);
    public static native String SDL_strdup (String str);
    public static native String SDL_strrev (String str);
    public static native String SDL_strupr (String str);
    public static native String SDL_strlwr (String str);
    public static native String SDL_strchr (String str,int c);
    public static native String SDL_strrchr (String str,int c);
    public static native String SDL_strstr (String haystack,String needle);
    public static native String SDL_itoa (int value,String str,int radix);
    public static native String SDL_uitoa (int value,String str,int radix);
    public static native String SDL_lltoa (long value,String str,int radix);
    public static native String SDL_ulltoa (long value,String str,int radix);
    public static native int SDL_atoi (String str);
    public static native double SDL_atof (String str);
    public static native int SDL_strcmp (String str1,String str2);
    public static native int SDL_strcasecmp (String str1,String str2);
    public static native double SDL_acos (double x);
    public static native double SDL_asin (double x);
    public static native double SDL_atan (double x);
    public static native double SDL_atan2 (double x,double y);
    public static native double SDL_ceil (double x);
    public static native double SDL_copysign (double x,double y);
    public static native double SDL_cos (double x);
    public static native float SDL_cosf (float x);
    public static native double SDL_fabs (double x);
    public static native double SDL_floor (double x);
    public static native double SDL_log (double x);
    public static native double SDL_pow (double x,double y);
    public static native double SDL_scalbn (double x,int n);
    public static native double SDL_sin (double x);
    public static native float SDL_sinf (float x);
    public static native double SDL_sqrt (double x);
    public static native String SDL_iconv_string (String tocode,String fromcode,String inbuf,long inbytesleft);
    public static native void SDL_GetVersion (SDL_version ver);
    public static native int SDL_GetRevisionNumber ();
    public static native SDL_Surface_byref SDL_CreateRGBSurfaceFrom (Pointer pixels,int width,int height,int depth,int pitch,int Rmask,int Gmask,int Bmask,int Amask);
    public static native void SDL_FreeSurface (SDL_Surface surface);
    public static native int SDL_SetSurfacePalette (SDL_Surface surface,SDL_Palette palette);
    public static native int SDL_LockSurface (SDL_Surface surface);
    public static native void SDL_UnlockSurface (SDL_Surface surface);
    public static native SDL_Surface_byref SDL_LoadBMP_RW (SDL_RWops src,int freesrc);
    public static native int SDL_SetSurfaceRLE (SDL_Surface surface,int flag);
    public static native int SDL_SetColorKey (SDL_Surface surface,int flag,int key);
    public static native int SDL_GetColorKey (SDL_Surface surface,Pointer key);
    public static native int SDL_SetSurfaceColorMod (SDL_Surface surface,byte r,byte g,byte b);
    public static native int SDL_GetSurfaceColorMod (SDL_Surface surface,Pointer r,Pointer g,Pointer b);
    public static native int SDL_SetSurfaceAlphaMod (SDL_Surface surface,byte alpha);
    public static native int SDL_GetSurfaceAlphaMod (SDL_Surface surface,Pointer alpha);
    public static native int SDL_SetSurfaceBlendMode (SDL_Surface surface,int blendMode);
    public static native int SDL_GetSurfaceBlendMode (SDL_Surface surface,Pointer blendMode);
    public static native int SDL_SetClipRect (SDL_Surface surface,SDL_Rect rect);
    public static native void SDL_GetClipRect (SDL_Surface surface,SDL_Rect rect);
    public static native int SDL_ConvertPixels (int width,int height,int src_format,Pointer src,int src_pitch,int dst_format,Pointer dst,int dst_pitch);
    public static native int SDL_SoftStretch (SDL_Surface src,SDL_Rect srcrect,SDL_Surface dst,SDL_Rect dstrect);
    public static native int SDL_GetPowerInfo (Pointer secs,Pointer pct);
    public static native int SDL_NumJoysticks ();
    public static native SDL_Joystick_byref SDL_JoystickOpen (int device_index);
    public static native SDL_JoystickGUID_byvalue SDL_JoystickGetDeviceGUID (int device_index);
    public static native SDL_JoystickGUID_byvalue SDL_JoystickGetGUID (SDL_Joystick joystick);
    public static native void SDL_JoystickGetGUIDString (SDL_JoystickGUID_byvalue guid,String pszGUID,int cbGUID);
    public static native SDL_JoystickGUID_byvalue SDL_JoystickGetGUIDFromString (String pchGUID);
    public static native int SDL_JoystickGetAttached (SDL_Joystick joystick);
    public static native int SDL_JoystickInstanceID (SDL_Joystick joystick);
    public static native int SDL_JoystickNumAxes (SDL_Joystick joystick);
    public static native int SDL_JoystickNumBalls (SDL_Joystick joystick);
    public static native int SDL_JoystickNumHats (SDL_Joystick joystick);
    public static native int SDL_JoystickNumButtons (SDL_Joystick joystick);
    public static native void SDL_JoystickUpdate ();
    public static native int SDL_JoystickEventState (int state);
    public static native short SDL_JoystickGetAxis (SDL_Joystick joystick,int axis);
    public static native byte SDL_JoystickGetHat (SDL_Joystick joystick,int hat);
    public static native int SDL_JoystickGetBall (SDL_Joystick joystick,int ball,Pointer dx,Pointer dy);
    public static native byte SDL_JoystickGetButton (SDL_Joystick joystick,int button);
    public static native void SDL_JoystickClose (SDL_Joystick joystick);
    public static native SDL_Window_byref SDL_GetKeyboardFocus ();
    public static native int SDL_GetModState ();
    public static native void SDL_SetModState (int modstate);
    public static native int SDL_GetKeyFromScancode (int scancode);
    public static native int SDL_GetScancodeFromKey (int key);
    public static native int SDL_GetScancodeFromName (String name);
    public static native int SDL_GetKeyFromName (String name);
    public static native void SDL_StartTextInput ();
    public static native int SDL_IsTextInputActive ();
    public static native void SDL_StopTextInput ();
    public static native void SDL_SetTextInputRect (SDL_Rect rect);
    public static native int SDL_HasScreenKeyboardSupport ();
    public static native int SDL_IsScreenKeyboardShown (SDL_Window window);
    public static native int SDL_LockMutex (SDL_mutex mutex);
    public static native int SDL_TryLockMutex (SDL_mutex mutex);
    public static native int SDL_UnlockMutex (SDL_mutex mutex);
    public static native void SDL_DestroyMutex (SDL_mutex mutex);
    public static native void SDL_DestroySemaphore (SDL_sem sem);
    public static native int SDL_SemWait (SDL_sem sem);
    public static native int SDL_SemTryWait (SDL_sem sem);
    public static native int SDL_SemWaitTimeout (SDL_sem sem,int ms);
    public static native int SDL_SemPost (SDL_sem sem);
    public static native int SDL_SemValue (SDL_sem sem);
    public static native void SDL_DestroyCond (SDL_cond cond);
    public static native int SDL_CondSignal (SDL_cond cond);
    public static native int SDL_CondBroadcast (SDL_cond cond);
    public static native int SDL_CondWait (SDL_cond cond,SDL_mutex mutex);
    public static native int SDL_CondWaitTimeout (SDL_cond cond,SDL_mutex mutex,int ms);
    public static native Pointer SDL_LoadObject (String sofile);
    public static native Pointer SDL_LoadFunction (Pointer handle,String name);
    public static native void SDL_UnloadObject (Pointer handle);
    public static native int SDL_GetWindowWMInfo (SDL_Window window,SDL_SysWMinfo info);
    public static native int SDL_SetHintWithPriority (String name,String value,int priority);
    public static native int SDL_SetHint (String name,String value);
    public static native void SDL_ClearHints ();
    public static final int SDLK_SPACE= (int)' ' ;
    public static final int SDLK_EXCLAIM= (int)'!' ;
    public static final int SDLK_QUOTEDBL= (int)'"' ;
    public static final int SDLK_HASH= (int)'#' ;
    public static final int SDLK_PERCENT= (int)'%' ;
    public static final int SDLK_DOLLAR= (int)'$' ;
    public static final int SDLK_AMPERSAND= (int)'&' ;
    public static final int SDLK_QUOTE= (int)'\'' ;
    public static final int SDLK_LEFTPAREN= (int)'(' ;
    public static final int SDLK_RIGHTPAREN= (int)')' ;
    public static final int SDLK_ASTERISK= (int)'*' ;
    public static final int SDLK_PLUS= (int)'+' ;
    public static final int SDLK_COMMA= (int)',' ;
    public static final int SDLK_MINUS= (int)'-' ;
    public static final int SDLK_PERIOD= (int)'.' ;
    public static final int SDLK_SLASH= (int)'/' ;
    public static final int SDLK_0= (int)'0' ;
    public static final int SDLK_1= (int)'1' ;
    public static final int SDLK_2= (int)'2' ;
    public static final int SDLK_3= (int)'3' ;
    public static final int SDLK_4= (int)'4' ;
    public static final int SDLK_5= (int)'5' ;
    public static final int SDLK_6= (int)'6' ;
    public static final int SDLK_7= (int)'7' ;
    public static final int SDLK_8= (int)'8' ;
    public static final int SDLK_9= (int)'9' ;
    public static final int SDLK_COLON= (int)':' ;
    public static final int SDLK_SEMICOLON= (int)';' ;
    public static final int SDLK_LESS= (int)'<' ;
    public static final int SDLK_EQUALS= (int)'=' ;
    public static final int SDLK_GREATER= (int)'>' ;
    public static final int SDLK_QUESTION= (int)'?' ;
    public static final int SDLK_AT= (int)'@' ;
    public static final int SDLK_LEFTBRACKET= (int)'[' ;
    public static final int SDLK_RIGHTBRACKET= (int)']' ;
    public static final int SDLK_CARET= (int)'^' ;
    public static final int SDLK_UNDERSCORE= (int)'_' ;
    public static final int SDLK_BACKQUOTE= (int)'`' ;
    public static final int SDLK_a= (int)'a' ;
    public static final int SDLK_b= (int)'b' ;
    public static final int SDLK_c= (int)'c' ;
    public static final int SDLK_d= (int)'d' ;
    public static final int SDLK_e= (int)'e' ;
    public static final int SDLK_f= (int)'f' ;
    public static final int SDLK_g= (int)'g' ;
    public static final int SDLK_h= (int)'h' ;
    public static final int SDLK_i= (int)'i' ;
    public static final int SDLK_j= (int)'j' ;
    public static final int SDLK_k= (int)'k' ;
    public static final int SDLK_l= (int)'l' ;
    public static final int SDLK_m= (int)'m' ;
    public static final int SDLK_n= (int)'n' ;
    public static final int SDLK_o= (int)'o' ;
    public static final int SDLK_p= (int)'p' ;
    public static final int SDLK_q= (int)'q' ;
    public static final int SDLK_r= (int)'r' ;
    public static final int SDLK_s= (int)'s' ;
    public static final int SDLK_t= (int)'t' ;
    public static final int SDLK_u= (int)'u' ;
    public static final int SDLK_v= (int)'v' ;
    public static final int SDLK_w= (int)'w' ;
    public static final int SDLK_x= (int)'x' ;
    public static final int SDLK_y= (int)'y' ;
    public static final int SDLK_z= (int)'z' ;
    public static final int SDLK_CAPSLOCK= (int) (SDL_SCANCODE_CAPSLOCK  | (1<<30) );
    public static final int SDLK_F1= (int) (SDL_SCANCODE_F1  | (1<<30) );
    public static final int SDLK_F2= (int) (SDL_SCANCODE_F2  | (1<<30) );
    public static final int SDLK_F3= (int) (SDL_SCANCODE_F3  | (1<<30) );
    public static final int SDLK_F4= (int) (SDL_SCANCODE_F4  | (1<<30) );
    public static final int SDLK_F5= (int) (SDL_SCANCODE_F5  | (1<<30) );
    public static final int SDLK_F6= (int) (SDL_SCANCODE_F6  | (1<<30) );
    public static final int SDLK_F7= (int) (SDL_SCANCODE_F7  | (1<<30) );
    public static final int SDLK_F8= (int) (SDL_SCANCODE_F8  | (1<<30) );
    public static final int SDLK_F9= (int) (SDL_SCANCODE_F9  | (1<<30) );
    public static final int SDLK_F10= (int) (SDL_SCANCODE_F10  | (1<<30) );
    public static final int SDLK_F11= (int) (SDL_SCANCODE_F11  | (1<<30) );
    public static final int SDLK_F12= (int) (SDL_SCANCODE_F12  | (1<<30) );
    public static final int SDLK_PRINTSCREEN= (int) (SDL_SCANCODE_PRINTSCREEN  | (1<<30) );
    public static final int SDLK_SCROLLLOCK= (int) (SDL_SCANCODE_SCROLLLOCK  | (1<<30) );
    public static final int SDLK_PAUSE= (int) (SDL_SCANCODE_PAUSE  | (1<<30) );
    public static final int SDLK_INSERT= (int) (SDL_SCANCODE_INSERT  | (1<<30) );
    public static final int SDLK_HOME= (int) (SDL_SCANCODE_HOME  | (1<<30) );
    public static final int SDLK_PAGEUP= (int) (SDL_SCANCODE_PAGEUP  | (1<<30) );
    public static final int SDLK_END= (int) (SDL_SCANCODE_END  | (1<<30) );
    public static final int SDLK_PAGEDOWN= (int) (SDL_SCANCODE_PAGEDOWN  | (1<<30) );
    public static final int SDLK_RIGHT= (int) (SDL_SCANCODE_RIGHT  | (1<<30) );
    public static final int SDLK_LEFT= (int) (SDL_SCANCODE_LEFT  | (1<<30) );
    public static final int SDLK_DOWN= (int) (SDL_SCANCODE_DOWN  | (1<<30) );
    public static final int SDLK_UP= (int) (SDL_SCANCODE_UP  | (1<<30) );
    public static final int SDLK_NUMLOCKCLEAR= (int) (SDL_SCANCODE_NUMLOCKCLEAR  | (1<<30) );
    public static final int SDLK_KP_DIVIDE= (int) (SDL_SCANCODE_KP_DIVIDE  | (1<<30) );
    public static final int SDLK_KP_MULTIPLY= (int) (SDL_SCANCODE_KP_MULTIPLY  | (1<<30) );
    public static final int SDLK_KP_MINUS= (int) (SDL_SCANCODE_KP_MINUS  | (1<<30) );
    public static final int SDLK_KP_PLUS= (int) (SDL_SCANCODE_KP_PLUS  | (1<<30) );
    public static final int SDLK_KP_ENTER= (int) (SDL_SCANCODE_KP_ENTER  | (1<<30) );
    public static final int SDLK_KP_1= (int) (SDL_SCANCODE_KP_1  | (1<<30) );
    public static final int SDLK_KP_2= (int) (SDL_SCANCODE_KP_2  | (1<<30) );
    public static final int SDLK_KP_3= (int) (SDL_SCANCODE_KP_3  | (1<<30) );
    public static final int SDLK_KP_4= (int) (SDL_SCANCODE_KP_4  | (1<<30) );
    public static final int SDLK_KP_5= (int) (SDL_SCANCODE_KP_5  | (1<<30) );
    public static final int SDLK_KP_6= (int) (SDL_SCANCODE_KP_6  | (1<<30) );
    public static final int SDLK_KP_7= (int) (SDL_SCANCODE_KP_7  | (1<<30) );
    public static final int SDLK_KP_8= (int) (SDL_SCANCODE_KP_8  | (1<<30) );
    public static final int SDLK_KP_9= (int) (SDL_SCANCODE_KP_9  | (1<<30) );
    public static final int SDLK_KP_0= (int) (SDL_SCANCODE_KP_0  | (1<<30) );
    public static final int SDLK_KP_PERIOD= (int) (SDL_SCANCODE_KP_PERIOD  | (1<<30) );
    public static final int SDLK_APPLICATION= (int) (SDL_SCANCODE_APPLICATION  | (1<<30) );
    public static final int SDLK_POWER= (int) (SDL_SCANCODE_POWER  | (1<<30) );
    public static final int SDLK_KP_EQUALS= (int) (SDL_SCANCODE_KP_EQUALS  | (1<<30) );
    public static final int SDLK_F13= (int) (SDL_SCANCODE_F13  | (1<<30) );
    public static final int SDLK_F14= (int) (SDL_SCANCODE_F14  | (1<<30) );
    public static final int SDLK_F15= (int) (SDL_SCANCODE_F15  | (1<<30) );
    public static final int SDLK_F16= (int) (SDL_SCANCODE_F16  | (1<<30) );
    public static final int SDLK_F17= (int) (SDL_SCANCODE_F17  | (1<<30) );
    public static final int SDLK_F18= (int) (SDL_SCANCODE_F18  | (1<<30) );
    public static final int SDLK_F19= (int) (SDL_SCANCODE_F19  | (1<<30) );
    public static final int SDLK_F20= (int) (SDL_SCANCODE_F20  | (1<<30) );
    public static final int SDLK_F21= (int) (SDL_SCANCODE_F21  | (1<<30) );
    public static final int SDLK_F22= (int) (SDL_SCANCODE_F22  | (1<<30) );
    public static final int SDLK_F23= (int) (SDL_SCANCODE_F23  | (1<<30) );
    public static final int SDLK_F24= (int) (SDL_SCANCODE_F24  | (1<<30) );
    public static final int SDLK_EXECUTE= (int) (SDL_SCANCODE_EXECUTE  | (1<<30) );
    public static final int SDLK_HELP= (int) (SDL_SCANCODE_HELP  | (1<<30) );
    public static final int SDLK_MENU= (int) (SDL_SCANCODE_MENU  | (1<<30) );
    public static final int SDLK_SELECT= (int) (SDL_SCANCODE_SELECT  | (1<<30) );
    public static final int SDLK_STOP= (int) (SDL_SCANCODE_STOP  | (1<<30) );
    public static final int SDLK_AGAIN= (int) (SDL_SCANCODE_AGAIN  | (1<<30) );
    public static final int SDLK_UNDO= (int) (SDL_SCANCODE_UNDO  | (1<<30) );
    public static final int SDLK_CUT= (int) (SDL_SCANCODE_CUT  | (1<<30) );
    public static final int SDLK_COPY= (int) (SDL_SCANCODE_COPY  | (1<<30) );
    public static final int SDLK_PASTE= (int) (SDL_SCANCODE_PASTE  | (1<<30) );
    public static final int SDLK_FIND= (int) (SDL_SCANCODE_FIND  | (1<<30) );
    public static final int SDLK_MUTE= (int) (SDL_SCANCODE_MUTE  | (1<<30) );
    public static final int SDLK_VOLUMEUP= (int) (SDL_SCANCODE_VOLUMEUP  | (1<<30) );
    public static final int SDLK_VOLUMEDOWN= (int) (SDL_SCANCODE_VOLUMEDOWN  | (1<<30) );
    public static final int SDLK_KP_COMMA= (int) (SDL_SCANCODE_KP_COMMA  | (1<<30) );
    public static final int SDLK_ALTERASE= (int) (SDL_SCANCODE_ALTERASE  | (1<<30) );
    public static final int SDLK_SYSREQ= (int) (SDL_SCANCODE_SYSREQ  | (1<<30) );
    public static final int SDLK_CANCEL= (int) (SDL_SCANCODE_CANCEL  | (1<<30) );
    public static final int SDLK_CLEAR= (int) (SDL_SCANCODE_CLEAR  | (1<<30) );
    public static final int SDLK_PRIOR= (int) (SDL_SCANCODE_PRIOR  | (1<<30) );
    public static final int SDLK_RETURN2= (int) (SDL_SCANCODE_RETURN2  | (1<<30) );
    public static final int SDLK_SEPARATOR= (int) (SDL_SCANCODE_SEPARATOR  | (1<<30) );
    public static final int SDLK_OUT= (int) (SDL_SCANCODE_OUT  | (1<<30) );
    public static final int SDLK_OPER= (int) (SDL_SCANCODE_OPER  | (1<<30) );
    public static final int SDLK_CLEARAGAIN= (int) (SDL_SCANCODE_CLEARAGAIN  | (1<<30) );
    public static final int SDLK_CRSEL= (int) (SDL_SCANCODE_CRSEL  | (1<<30) );
    public static final int SDLK_EXSEL= (int) (SDL_SCANCODE_EXSEL  | (1<<30) );
    public static final int SDLK_KP_00= (int) (SDL_SCANCODE_KP_00  | (1<<30) );
    public static final int SDLK_KP_000= (int) (SDL_SCANCODE_KP_000  | (1<<30) );
    public static final int SDLK_CURRENCYUNIT= (int) (SDL_SCANCODE_CURRENCYUNIT  | (1<<30) );
    public static final int SDLK_KP_LEFTPAREN= (int) (SDL_SCANCODE_KP_LEFTPAREN  | (1<<30) );
    public static final int SDLK_KP_RIGHTPAREN= (int) (SDL_SCANCODE_KP_RIGHTPAREN  | (1<<30) );
    public static final int SDLK_KP_LEFTBRACE= (int) (SDL_SCANCODE_KP_LEFTBRACE  | (1<<30) );
    public static final int SDLK_KP_RIGHTBRACE= (int) (SDL_SCANCODE_KP_RIGHTBRACE  | (1<<30) );
    public static final int SDLK_KP_TAB= (int) (SDL_SCANCODE_KP_TAB  | (1<<30) );
    public static final int SDLK_KP_BACKSPACE= (int) (SDL_SCANCODE_KP_BACKSPACE  | (1<<30) );
    public static final int SDLK_KP_A= (int) (SDL_SCANCODE_KP_A  | (1<<30) );
    public static final int SDLK_KP_B= (int) (SDL_SCANCODE_KP_B  | (1<<30) );
    public static final int SDLK_KP_C= (int) (SDL_SCANCODE_KP_C  | (1<<30) );
    public static final int SDLK_KP_D= (int) (SDL_SCANCODE_KP_D  | (1<<30) );
    public static final int SDLK_KP_E= (int) (SDL_SCANCODE_KP_E  | (1<<30) );
    public static final int SDLK_KP_F= (int) (SDL_SCANCODE_KP_F  | (1<<30) );
    public static final int SDLK_KP_XOR= (int) (SDL_SCANCODE_KP_XOR  | (1<<30) );
    public static final int SDLK_KP_POWER= (int) (SDL_SCANCODE_KP_POWER  | (1<<30) );
    public static final int SDLK_KP_PERCENT= (int) (SDL_SCANCODE_KP_PERCENT  | (1<<30) );
    public static final int SDLK_KP_LESS= (int) (SDL_SCANCODE_KP_LESS  | (1<<30) );
    public static final int SDLK_KP_GREATER= (int) (SDL_SCANCODE_KP_GREATER  | (1<<30) );
    public static final int SDLK_KP_AMPERSAND= (int) (SDL_SCANCODE_KP_AMPERSAND  | (1<<30) );
    public static final int SDLK_KP_COLON= (int) (SDL_SCANCODE_KP_COLON  | (1<<30) );
    public static final int SDLK_KP_HASH= (int) (SDL_SCANCODE_KP_HASH  | (1<<30) );
    public static final int SDLK_KP_SPACE= (int) (SDL_SCANCODE_KP_SPACE  | (1<<30) );
    public static final int SDLK_KP_AT= (int) (SDL_SCANCODE_KP_AT  | (1<<30) );
    public static final int SDLK_KP_EXCLAM= (int) (SDL_SCANCODE_KP_EXCLAM  | (1<<30) );
    public static final int SDLK_KP_MEMSTORE= (int) (SDL_SCANCODE_KP_MEMSTORE  | (1<<30) );
    public static final int SDLK_KP_MEMRECALL= (int) (SDL_SCANCODE_KP_MEMRECALL  | (1<<30) );
    public static final int SDLK_KP_MEMCLEAR= (int) (SDL_SCANCODE_KP_MEMCLEAR  | (1<<30) );
    public static final int SDLK_KP_MEMADD= (int) (SDL_SCANCODE_KP_MEMADD  | (1<<30) );
    public static final int SDLK_KP_MEMDIVIDE= (int) (SDL_SCANCODE_KP_MEMDIVIDE  | (1<<30) );
    public static final int SDLK_KP_PLUSMINUS= (int) (SDL_SCANCODE_KP_PLUSMINUS  | (1<<30) );
    public static final int SDLK_KP_CLEAR= (int) (SDL_SCANCODE_KP_CLEAR  | (1<<30) );
    public static final int SDLK_KP_CLEARENTRY= (int) (SDL_SCANCODE_KP_CLEARENTRY  | (1<<30) );
    public static final int SDLK_KP_BINARY= (int) (SDL_SCANCODE_KP_BINARY  | (1<<30) );
    public static final int SDLK_KP_OCTAL= (int) (SDL_SCANCODE_KP_OCTAL  | (1<<30) );
    public static final int SDLK_KP_DECIMAL= (int) (SDL_SCANCODE_KP_DECIMAL  | (1<<30) );
    public static final int SDLK_LCTRL= (int) (SDL_SCANCODE_LCTRL  | (1<<30) );
    public static final int SDLK_LSHIFT= (int) (SDL_SCANCODE_LSHIFT  | (1<<30) );
    public static final int SDLK_LALT= (int) (SDL_SCANCODE_LALT  | (1<<30) );
    public static final int SDLK_LGUI= (int) (SDL_SCANCODE_LGUI  | (1<<30) );
    public static final int SDLK_RCTRL= (int) (SDL_SCANCODE_RCTRL  | (1<<30) );
    public static final int SDLK_RSHIFT= (int) (SDL_SCANCODE_RSHIFT  | (1<<30) );
    public static final int SDLK_RALT= (int) (SDL_SCANCODE_RALT  | (1<<30) );
    public static final int SDLK_RGUI= (int) (SDL_SCANCODE_RGUI  | (1<<30) );
    public static final int SDLK_MODE= (int) (SDL_SCANCODE_MODE  | (1<<30) );
    public static final int SDLK_AUDIONEXT= (int) (SDL_SCANCODE_AUDIONEXT  | (1<<30) );
    public static final int SDLK_AUDIOPREV= (int) (SDL_SCANCODE_AUDIOPREV  | (1<<30) );
    public static final int SDLK_AUDIOSTOP= (int) (SDL_SCANCODE_AUDIOSTOP  | (1<<30) );
    public static final int SDLK_AUDIOPLAY= (int) (SDL_SCANCODE_AUDIOPLAY  | (1<<30) );
    public static final int SDLK_AUDIOMUTE= (int) (SDL_SCANCODE_AUDIOMUTE  | (1<<30) );
    public static final int SDLK_MEDIASELECT= (int) (SDL_SCANCODE_MEDIASELECT  | (1<<30) );
    public static final int SDLK_WWW= (int) (SDL_SCANCODE_WWW  | (1<<30) );
    public static final int SDLK_MAIL= (int) (SDL_SCANCODE_MAIL  | (1<<30) );
    public static final int SDLK_CALCULATOR= (int) (SDL_SCANCODE_CALCULATOR  | (1<<30) );
    public static final int SDLK_COMPUTER= (int) (SDL_SCANCODE_COMPUTER  | (1<<30) );
    public static final int SDLK_AC_SEARCH= (int) (SDL_SCANCODE_AC_SEARCH  | (1<<30) );
    public static final int SDLK_AC_HOME= (int) (SDL_SCANCODE_AC_HOME  | (1<<30) );
    public static final int SDLK_AC_BACK= (int) (SDL_SCANCODE_AC_BACK  | (1<<30) );
    public static final int SDLK_AC_FORWARD= (int) (SDL_SCANCODE_AC_FORWARD  | (1<<30) );
    public static final int SDLK_AC_STOP= (int) (SDL_SCANCODE_AC_STOP  | (1<<30) );
    public static final int SDLK_AC_REFRESH= (int) (SDL_SCANCODE_AC_REFRESH  | (1<<30) );
    public static final int SDLK_AC_BOOKMARKS= (int) (SDL_SCANCODE_AC_BOOKMARKS  | (1<<30) );
    public static final int SDLK_BRIGHTNESSUP= (int) (SDL_SCANCODE_BRIGHTNESSUP  | (1<<30) );
    public static final int SDLK_DISPLAYSWITCH= (int) (SDL_SCANCODE_DISPLAYSWITCH  | (1<<30) );
    public static final int SDLK_KBDILLUMDOWN= (int) (SDL_SCANCODE_KBDILLUMDOWN  | (1<<30) );
    public static final int SDLK_KBDILLUMUP= (int) (SDL_SCANCODE_KBDILLUMUP  | (1<<30) );
    public static final int SDLK_EJECT= (int) (SDL_SCANCODE_EJECT  | (1<<30) );
    public static final int SDLK_SLEEP= (int) (SDL_SCANCODE_SLEEP  | (1<<30) );
}
