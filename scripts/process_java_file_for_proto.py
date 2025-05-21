import re
import os
import argparse
import fnmatch

def process_java_file(file_path):
    with open(file_path, 'r+', encoding='utf-8') as f:
        content = f.read()
        # 使用正则替换所有 FIELD_NUMBER 常量
        modified = re.sub(
            r'public static final int ([A-Z_]+)_FIELD_NUMBER = (\d+);',
            r'public static final int \1 = \2;',
            content
        )
        f.seek(0)
        f.write(modified)
        f.truncate()

def should_exclude(file_path, root_dir, exclude_patterns):
    # 获取相对于根目录的路径（统一使用 Linux 路径分隔符）
    relative_path = os.path.relpath(file_path, root_dir).replace(os.sep, '/')

    # 检查是否匹配任何排除模式
    return any(
        fnmatch.fnmatch(relative_path, pattern)
        for pattern in exclude_patterns
    )

def main():
    parser = argparse.ArgumentParser(description='Process Java proto files.')
    parser.add_argument('directory', help='Root directory to process Java files')
    parser.add_argument('--exclude',
                        action='append',
                        default=[],
                        help='Exclusion patterns (e.g. "com/example/*.java" or "*/test/*")')
    args = parser.parse_args()

    # 处理所有排除模式
    exclude_patterns = args.exclude if args.exclude else []

    # 遍历生成目录下的所有 Java 文件
    for root, dirs, files in os.walk(args.directory):
        for file in files:
            if file.endswith('.java'):
                full_path = os.path.join(root, file)
                if should_exclude(full_path, args.directory, exclude_patterns):
                    print(f"Skipping excluded file: {full_path}")
                    continue
                process_java_file(full_path)

if __name__ == '__main__':
    main()